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

import androidx.appcompat.app.AppCompatActivity;

import com.gylee.ipcdemo.R;
import com.gylee.ipcdemo.aidl.IBookManager;

import java.util.List;

public class BookManager2Activity extends AppCompatActivity {

    private static final String TAG = "BookManager2Activity";

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                List<Book> list = bookManager.obtainBookList();
                Log.e(TAG, "onServiceConnected: " + list.size());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
    }

    public void bindService(View view) {
        Intent intent = new Intent(this,BookMananger2Service.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }

}
