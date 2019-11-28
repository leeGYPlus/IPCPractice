package com.gylee.ipcdemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class CusService : Service() {

    var count = 0
    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
//      while (true){
          Log.e("CusService","num is ${count++}")
//
//          Thread.sleep(5000)
    }
}
