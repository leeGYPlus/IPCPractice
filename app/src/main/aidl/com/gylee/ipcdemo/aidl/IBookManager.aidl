// IBookManager.aidl
package com.gylee.ipcdemo.aidl;

import com.gylee.ipcdemo.aidl.Book;

// Declare any non-default types here with import statements

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> obtainBookList();
    void addBook(in Book book);
}
