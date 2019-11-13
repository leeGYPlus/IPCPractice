// IOnNewBookArriveListener.aidl
package com.gylee.ipcdemo.aidl;

// Declare any non-default types here with import statements
import com.gylee.ipcdemo.aidl.Book;

interface IOnNewBookArriveListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    void onNewBookArrived(in Book newBook);
}
