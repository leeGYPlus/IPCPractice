package com.gylee.ipcdemo.socket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.util.PrintWriterPrinter
import android.view.View
import com.gylee.ipcdemo.R
import kotlinx.android.synthetic.main.activity_socket.*
import java.io.*
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class SocketActivity : AppCompatActivity() {


    private lateinit var mPrinterWriter: PrintWriter
    private lateinit var mClientSocket: Socket

    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MESSAGE_RECEIVE_NEW_MSG ->
                    content.text = "${content.text} ${msg.obj}"
                MESSAGE_SOCKET_CONNECTED -> button.isEnabled = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socket)
        startService(Intent(this, SocketService::class.java))
        openSocketThread()
    }

    // 在子线程进行 Socket 相关操作
    private fun openSocketThread() {
        Thread {
            // 创建客户端 Socket
            var socket: Socket? = null
            while (socket == null) {
                try {
                    socket = Socket("localhost", 8066)
                    mClientSocket = socket
                    mPrinterWriter =
                        PrintWriter(
                            BufferedWriter(OutputStreamWriter(socket.getOutputStream())),
                            true
                        )
                    mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED)
                    Log.e("SocketActivity", "Server connect success")
                } catch (e: IOException) {
                    SystemClock.sleep(1000L)
                    Log.e("SocketActivity", "connect tcp server failed,retry...")
                }
            }

            // 接收服务器端的消息
            try {
                val br = BufferedReader(InputStreamReader(socket.getInputStream()));
                while (!this.isFinishing) {
                    val msg = br.readLine()
                    Log.e("SocketActivity", "receive: $msg")
                    msg?.let {
                        val time = formatDateTim(System.currentTimeMillis())
                        val showMessage = "server $time: $msg \n"
                        mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showMessage).sendToTarget()
                    }
                }
                Log.e("SocketActivity", "quit...")
                MyUtils.close(mPrinterWriter)
                MyUtils.close(br)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }



    // 向服务端发送消息
    fun click(view: View) {
        val msg = edit.text.toString()
        if (msg.isNotEmpty()) {
            Executors.newFixedThreadPool(2).execute{
                mPrinterWriter.println(msg)
            }
            edit.setText("")
            val time = formatDateTim(System.currentTimeMillis())
            val showMessage = "self: $time : $msg \n"
            content.text = "${content.text} $showMessage"
        }
    }

    private fun formatDateTim(time: Long) =
        SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(Date(time))

    override fun onDestroy() {
        mClientSocket?.let {
            it.shutdownInput()
            it.close()
        }

        super.onDestroy()
    }

    companion object {
        const val MESSAGE_RECEIVE_NEW_MSG = 1
        const val MESSAGE_SOCKET_CONNECTED = 2
    }
}
