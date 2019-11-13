package com.gylee.ipcdemo.aidl

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.util.concurrent.CopyOnWriteArrayList

class BookManagerService : Service() {

    private val mBookList = CopyOnWriteArrayList<Book>()

//    private val mBinder: Binder =
//        object : com.gylee.ipcdemo.aidl.IBookManager.Stub() {
//            override fun unregisterListener(listener: IOnNewBookArriveListener?) {
//            }
//
//            override fun registerListener(listener: IOnNewBookArriveListener?) {
//            }
//
//            override fun obtainBookList(): MutableList<Book> = mBookList
//
//            override fun addBook(book: Book?) {
//                mBookList.add(book)
//            }
//        }


//    override fun onCreate() {
//        super.onCreate()
//        mBookList.add(Book(1, "Android"))
//        mBookList.add(Book(2, "Ios"))
//    }
//
    override fun onBind(intent: Intent?): IBinder? = null
//
//    companion object {
//        const val TAG = "BookManagerService"
//    }
}