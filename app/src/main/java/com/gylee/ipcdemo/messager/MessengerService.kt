package com.gylee.ipcdemo.messager

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import com.gylee.ipcdemo.MAG_FROM_CLIENT

class MessengerService : Service() {

    private val messenger = Messenger(MessengerHandler())

    override fun onBind(intent: Intent?): IBinder?  = messenger.binder

    companion object {
        const val TAG = "MessengerService"

        class MessengerHandler : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MAG_FROM_CLIENT -> {
                        Log.i(TAG, "receive message from client： ${msg.data.getString("msg")}")
                    }
                    else -> {
                        super.handleMessage(msg)
                    }
                }
            }
        }
    }
}