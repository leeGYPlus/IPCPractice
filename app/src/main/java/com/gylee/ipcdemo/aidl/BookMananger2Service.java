package com.gylee.ipcdemo.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.gylee.ipcdemo.aidl.IBookManager;
import com.gylee.ipcdemo.aidl.IOnNewBookArriveListener;

public class BookMananger2Service extends Service {
    private static final String TAG = "BookMananger2Service";

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList();
    private CopyOnWriteArrayList<com.gylee.ipcdemo.aidl.IOnNewBookArriveListener> listenerList = new CopyOnWriteArrayList();

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> obtainBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArriveListener listener) throws RemoteException {

            if(!listenerList.contains(listener)){
                listenerList.add(listener);
            }else {
                Log.e(TAG, "registerListener: listener is exist");
            }
            Log.e(TAG, "registerListener: listener size" + listenerList.size() );
        }

        @Override
        public void unregisterListener(com.gylee.ipcdemo.aidl.IOnNewBookArriveListener listener) throws RemoteException {

            if(listenerList.contains(listener)){
                listenerList.remove(listener);
                Log.e(TAG, "unregisterListener: unregister success" );
            }else {
                Log.e(TAG, "unregisterListener: listener is not exist");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new ServiceWorker()).start();
    }

    private class ServiceWorker implements Runnable{

        @Override
        public void run() {

            while (!mIsServiceDestoryed.get()){
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId = bookList.size();
                Book newBook = new Book(bookId,"Android" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onNewBookArrived(Book newBook) throws RemoteException {
        bookList.add(newBook);
        Log.e(TAG, "Service onNewBookArrived: " + Thread.currentThread().getName()  );
        for(IOnNewBookArriveListener listener:listenerList){
            listener.onNewBookArrived(newBook);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}
