package com.gylee.ipcdemo.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gylee.ipcdemo.R;
import com.gylee.ipcdemo.aidl.IBookManager;
import com.gylee.ipcdemo.aidl.IOnNewBookArriveListener;

import java.util.List;

import static com.gylee.ipcdemo.ConstantsKt.MESSAGE_NEW_BOOK_ARRIVED;

/**
 * 通过 AIDL 实现跨进程通信
 *
 * AIDL 分为服务端和客户端
 *
 * 1. 服务端
 *
 *      服务端首先要创建一个 Service 用来监听客户端的连接请求，在本例中为 BookMananger2Service。
 * 然后创建一个 AIDL 文件，将暴露给客户端的接口在 AIDL 中声明，最后在 Service 中实现 AIDL 这些接口，即 BookMananger2Service 中的new IBookManager.Stub() 匿名对象
 *
 * 2. 客户端
 *
 * 首先客户端应该绑定服务端的 Service 即 BookMananger2Service，绑定成功后，将服务端返回的 Binder 对象转换为 AIDL接口所属的类型，此处为 IBookManager，
 * 那么此时客户端就可以通过这个转换后的的对象引用调用 AIDL 的方法了。
 */

public class BookManager2Activity extends AppCompatActivity {
    private int bookId = 0;

    private static final String TAG = "BookManager2Activity";
    private TextView showInfo;

    private IBookManager bookManager;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // asInterface 用于将服务端端 Binder 对象转换为客户端所需要的 AIDL 接口类型对象。
            // 如果是同一个进程，那么此处返回的应该为 BookMananger2Service  中的 mBinder 对象，
            //如果是不同的进程，那么返回的实例对象应该为 IBookManager.Stub.Proxy 对象，之后经过 IPC 调用会调用 mBinder 中的相应方法
            bookManager = IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bookManager = null;
        }
    };

    private static class NewBookHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.e(TAG, "handleMessage:receive a new book " + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private Handler handler = new NewBookHandler();

    // 因为 onNewBookArriveListener 的 onNewBookArrived 方法最终是在 IBookManager.Stub.onNewBookArrived 执行，
    // 所以onNewBookArrived 方法运行在 Binder 线程池中，需要 Handler 向主线程发送后才可以更新 UI
    private IOnNewBookArriveListener onNewBookArriveListener = new IOnNewBookArriveListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Log.e(TAG, "onNewBookArrived: thread:" + Thread.currentThread().getName() );
            handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        showInfo = findViewById(R.id.showInfo);
        Intent intent = new Intent(this, BookMananger2Service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void bindService(View view) {
        Intent intent = new Intent(this, BookMananger2Service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void addBook(View view) throws RemoteException {
        Book book = new Book(bookId, "Android Book:" + bookId);
        bookManager.addBook(book);
        bookId = bookManager.obtainBookList().size();
        bookManager.registerListener(onNewBookArriveListener);
    }

    public void getBook(View view) {
        try {
            List<Book> list = bookManager.obtainBookList();
            showInfo.setText(list.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (bookManager != null && bookManager.asBinder().isBinderAlive()) {
            try {
                Log.e(TAG, "onDestroy: unregister listener:" + onNewBookArriveListener);
                bookManager.unregisterListener(onNewBookArriveListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        unbindService(connection);
        super.onDestroy();
    }
}
