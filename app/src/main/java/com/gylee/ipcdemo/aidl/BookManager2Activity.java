package com.gylee.ipcdemo.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gylee.ipcdemo.R;
import com.gylee.ipcdemo.aidl.IBookManager;

import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        showInfo = findViewById(R.id.showInfo);
    }

    public void bindService(View view) {
        Intent intent = new Intent(this, BookMananger2Service.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void addBook(View view) throws RemoteException {
        Book book = new Book(bookId, "Android Book:" + bookId);
        bookManager.addBook(book);
        bookId++;
    }

    public void getBook(View view) {
        try {
            List<Book> list = bookManager.obtainBookList();
            showInfo.setText(list.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
