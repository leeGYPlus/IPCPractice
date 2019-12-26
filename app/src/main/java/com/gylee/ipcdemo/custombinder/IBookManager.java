package com.gylee.ipcdemo.custombinder;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.gylee.ipcdemo.aidl.Book;
import com.gylee.ipcdemo.binderpool.IBinderPool;

import java.util.List;

public interface IBookManager extends IInterface {
    static final String DESCRIPTOR = "class is IBookManager";
    final int TRANSACTION_ADDBOOK = IBinder.FIRST_CALL_TRANSACTION + 0;
    final int TRANSACTION_GETBOOKLIST = IBinder.FIRST_CALL_TRANSACTION + 1;
    public List<Book> getBookList() throws RemoteException;
    public void addBook(Book newBook);
}
