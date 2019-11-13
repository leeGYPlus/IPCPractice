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

public class BookManager2Activity extends AppCompatActivity {
    private int bookId = 0;

    private static final String TAG = "BookManager2Activity";
    private TextView showInfo;

    private IBookManager bookManager;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
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
