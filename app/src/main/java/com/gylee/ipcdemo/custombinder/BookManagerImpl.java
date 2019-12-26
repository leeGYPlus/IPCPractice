package com.gylee.ipcdemo.custombinder;

import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.gylee.ipcdemo.aidl.Book;

import java.util.List;

public class BookManagerImpl extends Binder implements IBookManager {
    @Override
    public List<Book> getBookList() throws RemoteException {
        return null;
    }

    @Override
    public void addBook(Book newBook) {

    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
