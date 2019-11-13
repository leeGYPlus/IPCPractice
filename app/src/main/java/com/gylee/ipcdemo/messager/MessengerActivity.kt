package com.gylee.ipcdemo.messager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import com.gylee.ipcdemo.MAG_FROM_CLIENT
import com.gylee.ipcdemo.R

class MessengerActivity : AppCompatActivity() {

    private lateinit var mService : Messenger

    private val connection by lazy {
        object:ServiceConnection{
            override fun onServiceDisconnected(name: ComponentName?) {
                mService
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mService = Messenger(service)
                val message = Message.obtain(null,MAG_FROM_CLIENT)
                val data = Bundle()
                data.putString("msg","hello, i am client")
                message.data = data
                mService.send(message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)
        val intent = Intent(this,MessengerService::class.java)
        bindService(intent,connection,Context.BIND_AUTO_CREATE)

    }


    override fun onDestroy() {
        unbindService(connection)
        super.onDestroy()
    }

    companion object{
        const val TAG = "MessengerActivity"
    }
}
