package com.example.outdoorsport.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.example.outdoorsport.R
import com.example.outdoorsport.utils.StopWatchInstance.setClockTickedListener
import com.example.outdoorsport.utils.StopWatchInstance.sync
import com.example.outdoorsport.presentation.fragments.Game1Fragment
import java.sql.DriverManager

class TimerService : Service() {
    private val  NOTIFICATION_CHANNEL = "Clock Service Channel"
    private lateinit var notificationManager: NotificationManager
    private lateinit var mBuilder: Notification.Builder
    override fun onCreate() {
        super.onCreate()
        val context: Context = this
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        mBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, NOTIFICATION_CHANNEL)
        } else {
            Notification.Builder(this)
        }
        mBuilder.setContentIntent(
            PendingIntent.getActivity(
                this,
                0,
                Intent(context, Game1Fragment::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        isRunning = true
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Clocks")
        // .setContentText(StopWatchInstance.getTime(runningTimer))
        startForeground(1, mBuilder.build())
        setClockTickedListener { time: String, running: Int ->
            DriverManager.println(" TimerService running:  $running")
            when (running) {
                StopWatchInstance.TIMER1 -> {
                    mBuilder.setContentTitle("Timer 1")
                }

                StopWatchInstance.TIMER2 -> {
                    mBuilder.setContentTitle("Timer 2")
                }

                StopWatchInstance.TIMER3 -> {
                    mBuilder.setContentTitle("Timer 3")
                }

                StopWatchInstance.TIMER4 -> {
                    mBuilder.setContentTitle("Timer 4")
                }
            }
            if (running != StopWatchInstance.NOT_RUNNING) {
                sync()
                mBuilder.setContentText(time)
                notificationManager.notify(1, mBuilder.build())
            }
        }
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                "Clocks",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                setShowBadge(false)
                setSound(null, null)
            }
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        println("onTaskRemoved called")
        this.stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        stopSelf()
        val broadcastIntent = Intent(this, SensorRestarterBroadcastReceiver::class.java)

        sendBroadcast(broadcastIntent)
    }

    companion object {
        var isRunning = false
    }
}