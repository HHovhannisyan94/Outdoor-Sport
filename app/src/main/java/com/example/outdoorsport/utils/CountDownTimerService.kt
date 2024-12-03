package com.example.outdoorsport.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import com.example.outdoorsport.R
import com.example.outdoorsport.presentation.fragments.Game2Fragment

class CountDownTimerService : Service() {
    var bi = Intent(COUNTDOWN_BR)
    private lateinit var cdt: CountDownTimer
    private lateinit var mBuilder: Notification.Builder
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        val context: Context = this

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        mBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            Notification.Builder(this)
        }
        Log.i(TAG, "Starting timer...")
        cdt = object : CountDownTimer(milliSec, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000)
                bi.apply {
                    putExtra("countdown", millisUntilFinished)
                    putExtra("countdownTimerRunning", true)
                    putExtra("countdownTimerFinished", false)
                }

                sendBroadcast(bi)
            }

            override fun onFinish() {
                Log.i(TAG, "Timer finished")
                bi.putExtra("countdownTimerFinished", true)
                sendBroadcast(bi)
                stopForeground(true)
                stopSelf()
            }
        }
        cdt.start()
        mBuilder.setContentIntent(
            PendingIntent.getActivity(
                this, 1, Intent(
                    context,
                    Game2Fragment::class.java
                ), PendingIntent.FLAG_IMMUTABLE
            )
        )

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        println("onTaskRemoved called")
        //stop service

        this.stopSelf()
    }

    override fun onDestroy() {
        cdt.cancel()
        isRunning = false
        Log.i(TAG, "Timer cancelled")
        bi.putExtra("countdownTimerRunning", false)
        sendBroadcast(bi)
        this.stopSelf()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        isRunning = true
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        mBuilder
            .setContentTitle("Code Generation")
            .setContentText(StopWatchInstance.getCountDownTime(milliSec))
            .setSmallIcon(R.mipmap.ic_launcher)
        startForeground(2, mBuilder.build())

        StopWatchInstance.setGame2ClockTickedListener { time: String, running: Int ->
            println(" CountDownTimerService running:  $running")
            when (running) {
                StopWatchInstance.GAME2TIMER1 -> {
                    mBuilder.setContentTitle("Timer 1")
                }

                StopWatchInstance.GAME2TIMER2 -> {
                    mBuilder.setContentTitle("Timer 2")
                }

                StopWatchInstance.GAME2TIMER3 -> {
                    mBuilder.setContentTitle("Timer 3")
                }

                StopWatchInstance.GAME2TIMER4 -> {
                    mBuilder.setContentTitle("Timer 4")
                }
            }
            if (running != StopWatchInstance.GAME2NOT_RUNNING) {
                StopWatchInstance.syncCountDown()
                mBuilder.setContentText(time)
                notificationManager.notify(2, mBuilder.build())
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Code Generation Service Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                setShowBadge(false)
                setSound(null, null)
            }
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "CodeGenerationServiceChannel"
        private const val TAG = "CountDownTimerService"
        const val COUNTDOWN_BR = "com.example.outdoorsport"
        var milliSec = 0L
        var isRunning = false
    }
}
