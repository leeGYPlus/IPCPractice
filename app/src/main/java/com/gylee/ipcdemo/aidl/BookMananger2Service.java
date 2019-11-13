package com.gylee.ipcdemo.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.gylee.ipcdemo.aidl.IBookManager;

public class BookMananger2Service extends Service {

    private CopyOnWriteArrayList<Book> list = new CopyOnWriteArrayList();

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> obtainBookList() throws RemoteException {
            return list;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            list.add(book);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        list.add(new Book(1, "MIke"));
    }
}
