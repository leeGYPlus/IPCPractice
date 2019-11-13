package com.gylee.ipcdemo.messager

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.gylee.ipcdemo.MAG_FROM_CLIENT
import com.gylee.ipcdemo.MAG_FROM_SERVICE

class MessengerService : Service() {

    // MessengerHandler：处理从 Client 端来到信息
    private val messenger = Messenger(MessengerHandler())

    override fun onBind(intent: Intent?): IBinder? = messenger.binder

    companion object {
        const val TAG = "MessengerService"

        class MessengerHandler : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MAG_FROM_CLIENT -> {
                        Log.i(TAG, "receive message from client： ${msg.data.getString("msg")}")
                        // msg.replyTo：通过 Message 获得其发送对象： Messenger 对象
                        val messenger: Messenger = msg.replyTo
                        val replyMessage: Message = Message.obtain(null, MAG_FROM_SERVICE)
                        val data = Bundle()
                        data.putString("reply","hi,我已收到你的信息，稍后回复你")
                        replyMessage.data = data
                        messenger.send(replyMessage)
                    }
                    else -> {
                        super.handleMessage(msg)
                    }
                }
            }
        }
    }
}