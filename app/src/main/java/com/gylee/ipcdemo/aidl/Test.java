package com.gylee.ipcdemo.aidl;

import android.os.RemoteException;

import java.util.List;

public class Test extends com.gylee.ipcdemo.aidl.IBookManager.Stub {
    @Override
    public List<Book> obtainBookList() throws RemoteException {
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {

    }

    @Override
    public void registerListener(com.gylee.ipcdemo.aidl.IOnNewBookArriveListener listener) throws RemoteException {

    }

    @Override
    public void unregisterListener(com.gylee.ipcdemo.aidl.IOnNewBookArriveListener listener) throws RemoteException {

    }
}
