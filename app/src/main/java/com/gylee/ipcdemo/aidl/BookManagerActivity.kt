package com.gylee.ipcdemo.aidl

import androidx.appcompat.app.AppCompatActivity

class BookManagerActivity : AppCompatActivity() {

//    private val connection = object : ServiceConnection {
//        override fun onServiceDisconnected(name: ComponentName?) {
//        }
//
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            // 获得 IBookManager 对象
//            val bookManager: IBookManager =
//                IBookManager.Stub.asInterface(service)
//            // 操作 IBookManager 对象
//            val list = bookManager.obtainBookList()
//            Log.e(TAG,"query book list, list type is:${list.javaClass.canonicalName} ")
//            Log.e(TAG,"query book list:$list")
//        }
//
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_book_manager)
//    }
//
//    fun bindService(view: View) {
//        bindService(Intent(this, BookManagerService::class.java),connection,Context.BIND_AUTO_CREATE)
//    }
//
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unbindService(connection)
//    }
//
//
//    companion object {
//        const val TAG = "BookManagerActivity"
//
//    }

}
