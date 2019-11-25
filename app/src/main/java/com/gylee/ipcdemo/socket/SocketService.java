package com.gylee.ipcdemo.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class SocketService extends Service {

    private static final String TAG = "SocketService";
    private boolean mIsServiceDestoryed = false;
    private String[] mDefineMessage = new String[]{
            "hello.",
            "what's your name?",
            "have a nice day.",
            "have you eat something"
    };

    @Override
    public void onCreate() {
        new Thread(new TcpService()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed = true;
        super.onDestroy();
    }

    private class TcpService implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                // 监听本地 8066 端口
                serverSocket = new ServerSocket(8066);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            while (!mIsServiceDestoryed) {
                try {
                    // 接收客户端的请求
                    final Socket client = serverSocket.accept();
                    Log.e(TAG, "run: accept");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void responseClient(Socket client) throws IOException {
            // 用于接收客户端消息
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //用于向客户端发送消息
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
            out.println("欢迎来到聊天室");
            while (!mIsServiceDestoryed) {
                String str = in.readLine();
                Log.e(TAG, "msg from client:" + str);
                if(str == null){
                    // 客户端断开连接
                    break;
                }
                int index = new Random().nextInt(mDefineMessage.length);
                String msg = mDefineMessage[index];
                out.println(msg);
                Log.e(TAG, "send message: " + msg );
            }

            Log.e(TAG, "client quit.");
            MyUtils.close(out);
            MyUtils.close(in);
            client.close();
        }
    }
}
