package com.gylee.ipcdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Log.e("isRoot:",""+Root.isRootSystem())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startService(Intent(this, CusService::class.java))
            startForegroundService(Intent(this, CustomService::class.java))
        }
    }

    fun showNotifation() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("one", "first", NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = ContextCompat.getColor(this, R.color.colorAccent)
            channel.setShowBadge(true)
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
            channel.setBypassDnd(true)
            manager.createNotificationChannel(channel)

        }
    }
}
