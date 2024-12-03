package com.example.outdoorsport.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


internal class SensorRestarterBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.i(
            SensorRestarterBroadcastReceiver::class.java.simpleName,
            "Service Stops!"
        )
        context.startService(Intent(context, TimerService::class.java))
    }
}