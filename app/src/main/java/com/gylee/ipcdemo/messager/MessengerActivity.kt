package com.gylee.ipcdemo.messager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.gylee.ipcdemo.MAG_FROM_CLIENT
import com.gylee.ipcdemo.MAG_FROM_SERVICE
import com.gylee.ipcdemo.R

class MessengerActivity : AppCompatActivity() {

    //
    private lateinit var mService: Messenger
    private val mGetReplyMessenger:Messenger = Messenger(MessengerHandler())

    private val connection by lazy {
        object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mService = Messenger(service)
                val message = Message.obtain(null, MAG_FROM_CLIENT)
                val data = Bundle()
                data.putString("msg", "hello, i am client")
                message.data = data
                message.replyTo = mGetReplyMessenger
                // Messenger 跨进程发送消息，其中 IMessenger 为 AIDL 文件，借助 Binder 机制实现跨进程通信
                mService.send(message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)

    }

    fun bindService(view: View) {
        val intent = Intent(this, MessengerService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }


    override fun onDestroy() {
        unbindService(connection)
        super.onDestroy()
    }

    companion object {
        const val TAG = "MessengerActivity"
        class MessengerHandler : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MAG_FROM_SERVICE -> {
                        Log.i(TAG, "receive msg form service: ${msg.data.getString("reply")}")
                    }
                    else -> {
                        super.handleMessage(msg)
                    }
                }
            }
        }
    }

}
