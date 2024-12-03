package com.example.outdoorsport.utils

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import com.example.outdoorsport.data.interfaces.ClockStopListener
import com.example.outdoorsport.data.interfaces.ClockTickedListener
import com.example.outdoorsport.data.interfaces.Game2ClockTickedListener
import com.example.outdoorsport.data.interfaces.Game2OnClockStopListener
import com.example.outdoorsport.data.interfaces.ShowCodeListener
import java.io.IOException

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.properties.Delegates

object StopWatchInstance {
    const val TIMER1 = 1
    const val TIMER2 = 2
    const val TIMER3 = 3
    const val TIMER4 = 4
    const val TIMER_ALL = 5
    const val GAME2TIMER1 = 6
    const val GAME2TIMER2 = 7
    const val GAME2TIMER3 = 8
    const val GAME2TIMER4 = 9
    const val GAME2TIMER_ALL = 10
    const val NOT_RUNNING = -1
    const val GAME2NOT_RUNNING = -1

    var timer1 = 0
    var timer2 = 0
    var timer3 = 0
    var timer4 = 0
    var running = NOT_RUNNING

    var game2Running = GAME2NOT_RUNNING
    val handler = Handler(Looper.getMainLooper())
    private var clockTickedListener: ClockTickedListener? = null
    var mediaPlayer: MediaPlayer? = null
    private var clockStopListener: ClockStopListener? = null

    private var game2ClockTickedListener: Game2ClockTickedListener? = null
    private var game2OnClockStopListener: Game2OnClockStopListener? = null
    private var showCodeListener: ShowCodeListener? = null

    var mTimeLeftInMillis1 by Delegates.notNull<Long>()
    var mTimeLeftInMillis2 by Delegates.notNull<Long>()
    var mTimeLeftInMillis3 by Delegates.notNull<Long>()
    var mTimeLeftInMillis4 by Delegates.notNull<Long>()

    fun getTime(totalSeconds: Int): String {
        val seconds = totalSeconds % 60
        val totalMinutes = totalSeconds / 60
        val totalHours = totalMinutes / 60
        return String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            totalHours,
            totalMinutes % 60,
            seconds
        )
    }

    fun getCountDownTime(mTimeLeftInMillis: Long): String {
        val f: NumberFormat = DecimalFormat("00")
        //  val hour = mTimeLeftInMillis / 3600000 % 24
        val min = mTimeLeftInMillis / 60000 % 60
        val sec = mTimeLeftInMillis / 1000 % 60
        return f.format(min) + ":" + f.format(sec)
    }

    fun initMediaPlayer(context: Context) {
        mediaPlayer = MediaPlayer()
        try {
            val descriptor = context.assets.openFd("beep.mp3")
            mediaPlayer?.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )
            descriptor.close()
            mediaPlayer?.prepare()
        } catch (e: IOException) {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            try {
                mediaPlayer?.setDataSource(context, uri)
                mediaPlayer?.prepare()
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
            e.printStackTrace()
        }
        mediaPlayer?.setVolume(1f, 1f)
    }

    fun startTimer(i: Int) {
        if (running == i) return
        running = i
        handler.removeCallbacksAndMessages(null)
        mediaPlayer?.start()
        start()
    }

    fun startCountDownTimer(i: Int) {
        if (game2Running == i) return
        game2Running = i
        // handler.removeCallbacksAndMessages(null)
        mediaPlayer?.start()
        startDown()
    }

    fun stopTimer(context: Context) {
        mediaPlayer?.start()
        running = NOT_RUNNING
        handler.removeCallbacksAndMessages(null)
        saveState(context)
    }

    fun stopCountDownTimer(context: Context) {
        mediaPlayer?.start()
        game2Running = GAME2NOT_RUNNING
        // handler.removeCallbacksAndMessages(null)

        saveStateCodeGeneration(context)
    }

    private var startingTime = 0
    private var currentTime = 0
    private var timeThen: Long = 0
    private var runningTime = 0

    private fun init() {
        timeThen = System.currentTimeMillis()
        when (running) {
            TIMER1 -> {
                startingTime = timer1
            }

            TIMER2 -> {
                startingTime = timer2
            }

            TIMER3 -> {
                startingTime = timer3
            }

            TIMER4 -> {
                startingTime = timer4
            }
        }
    }

    private fun initCountDown() {
        when (game2Running) {
            GAME2TIMER1 -> {
                runningTime = GAME2TIMER1
            }

            GAME2TIMER2 -> {
                runningTime = GAME2TIMER2
            }

            GAME2TIMER3 -> {
                runningTime = GAME2TIMER3
            }

            GAME2TIMER4 -> {
                runningTime = GAME2TIMER4
            }
        }
    }

    private fun start() {
        init()
        handler.postDelayed(object : Runnable {
            override fun run() {
                when (running) {
                    TIMER1 -> {
                        timer1++
                        clockTickedListener?.onClockTick(
                            getTime(timer1), running
                        )
                    }

                    TIMER2 -> {
                        timer2++
                        clockTickedListener?.onClockTick(
                            getTime(timer2), running
                        )
                    }

                    TIMER3 -> {
                        timer3++
                        clockTickedListener?.onClockTick(
                            getTime(timer3), running
                        )
                    }

                    TIMER4 -> {
                        timer4++
                        clockTickedListener?.onClockTick(
                            getTime(timer4), running
                        )
                    }
                }
                if (running != NOT_RUNNING) {
                    handler.postDelayed(this, 1000)
                }
            }
        }, 1000)
    }

    private fun startDown() {
        initCountDown()

        when (game2Running) {
            GAME2TIMER1 -> {
                game2ClockTickedListener?.game2OnClockTick(
                    getCountDownTime(mTimeLeftInMillis1), game2Running
                )
            }

            GAME2TIMER2 -> {
                game2ClockTickedListener?.game2OnClockTick(
                    getCountDownTime(mTimeLeftInMillis2), game2Running
                )
            }

            GAME2TIMER3 -> {
                game2ClockTickedListener?.game2OnClockTick(
                    getCountDownTime(mTimeLeftInMillis3), game2Running
                )
            }

            GAME2TIMER4 -> {
                game2ClockTickedListener?.game2OnClockTick(
                    getCountDownTime(mTimeLeftInMillis4), game2Running
                )
            }
        }
    }

    private fun saveState(context: Context) {
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().apply {
            putInt("timer1", timer1).apply()
            putInt("timer2", timer2).apply()
            putInt("timer3", timer3).apply()
            putInt("timer4", timer4).apply()
        }
    }

    fun loadState(
        context: Context,
        timerTV1: AppCompatTextView,
        timerTV2: AppCompatTextView,
        timerTV3: AppCompatTextView,
        timerTV4: AppCompatTextView
    ) {
        if (running != NOT_RUNNING) return
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        timer1 = prefs.getInt("timer1", 0)
        timer2 = prefs.getInt("timer2", 0)
        timer3 = prefs.getInt("timer3", 0)
        timer4 = prefs.getInt("timer4", 0)
        timerTV1.text = getTime(timer1)
        timerTV2.text = getTime(timer2)
        timerTV3.text = getTime(timer3)
        timerTV4.text = getTime(timer4)
    }

    private fun saveStateCodeGeneration(context: Context) {
        context.getSharedPreferences("prefsCodeGen", Context.MODE_PRIVATE).edit().apply {
            putLong("Game2Timer1", mTimeLeftInMillis1).apply()
            putLong("Game2Timer2", mTimeLeftInMillis2).apply()
            putLong("Game2Timer3", mTimeLeftInMillis3).apply()
            putLong("Game2Timer4", mTimeLeftInMillis3).apply()
        }
    }

    fun loadStateCodeGeneration(
        context: Context,
        timerTV1: AppCompatTextView,
        timerTV2: AppCompatTextView,
        timerTV3: AppCompatTextView,
        timerTV4: AppCompatTextView
    ) {
        if (game2Running != GAME2NOT_RUNNING) return
        val prefs = context.getSharedPreferences("prefsCodeGen", Context.MODE_PRIVATE)
        val timer1 = prefs.getLong("Game2Timer1", 0)
        val timer2 = prefs.getLong("Game2Timer2", 0)
        val timer3 = prefs.getLong("Game2Timer3", 0)
        val timer4 = prefs.getLong("Game2Timer4", 0)
        timerTV1.text = getCountDownTime(timer1)
        timerTV2.text = getCountDownTime(timer2)
        timerTV3.text = getCountDownTime(timer3)
        timerTV4.text = getCountDownTime(timer4)
    }

    fun setClockTickedListener(clockTickedListener: ClockTickedListener) {
        StopWatchInstance.clockTickedListener = clockTickedListener
    }

    fun setStopListener(clockStopListener: ClockStopListener?) {
        StopWatchInstance.clockStopListener = clockStopListener
    }


    fun setGame2ClockTickedListener(game2ClockTickedListener: Game2ClockTickedListener) {
        StopWatchInstance.game2ClockTickedListener = game2ClockTickedListener
    }

    fun setGame2StopListener(game2OnClockStopListener: Game2OnClockStopListener?) {
        StopWatchInstance.game2OnClockStopListener = game2OnClockStopListener
    }


    fun reset(i: Int, context: Context) {
        when (i) {
            TIMER1 -> {
                timer1 = 0
                clockTickedListener?.onClockTick(
                    getTime(timer1), i
                )
            }

            TIMER2 -> {
                timer2 = 0
                clockTickedListener?.onClockTick(
                    getTime(timer2), i
                )
            }

            TIMER3 -> {
                timer3 = 0
                clockTickedListener?.onClockTick(
                    getTime(timer3), i
                )
            }

            TIMER4 -> {
                timer4 = 0
                clockTickedListener?.onClockTick(
                    getTime(timer4), i
                )
            }

            TIMER_ALL -> {
                timer4 = 0
                timer3 = 0
                timer2 = 0
                timer1 = 0
                clockTickedListener?.onClockTick(getTime(timer1), TIMER1)
                clockTickedListener?.onClockTick(getTime(timer2), TIMER2)
                clockTickedListener?.onClockTick(getTime(timer3), TIMER3)
                clockTickedListener?.onClockTick(getTime(timer4), TIMER4)
            }
        }
        saveState(context)
        if (running == i || i == TIMER_ALL) {
            if (running == i) {
                clockStopListener?.onClockStop(running)
            } else {
                clockStopListener?.onClockStop(TIMER_ALL)
            }
            running = NOT_RUNNING
            handler.removeCallbacksAndMessages(null)
        }
    }


    fun resetCountDown(i: Int, context: Context) {
        when (i) {
            GAME2TIMER1 -> {
                game2ClockTickedListener?.game2OnClockTick(
                    getCountDownTime(mTimeLeftInMillis1), i
                )
            }

            GAME2TIMER2 -> {
                game2ClockTickedListener?.game2OnClockTick(
                    getCountDownTime(mTimeLeftInMillis2), i
                )
            }

            GAME2TIMER3 -> {
                game2ClockTickedListener?.game2OnClockTick(
                    getCountDownTime(mTimeLeftInMillis3),
                    i
                )
            }

            GAME2TIMER4 -> {
                game2ClockTickedListener?.game2OnClockTick(
                    getCountDownTime(mTimeLeftInMillis4), i
                )
            }

            GAME2TIMER_ALL -> {
                game2ClockTickedListener?.apply {
                    game2OnClockTick(
                        getCountDownTime(mTimeLeftInMillis1),
                        GAME2TIMER1
                    )
                    game2OnClockTick(
                        getCountDownTime(mTimeLeftInMillis2),
                        GAME2TIMER2
                    )
                    game2OnClockTick(
                        getCountDownTime(mTimeLeftInMillis3),
                        GAME2TIMER3
                    )
                    game2OnClockTick(
                        getCountDownTime(mTimeLeftInMillis4),
                        GAME2TIMER4
                    )
                }
            }
        }
        saveStateCodeGeneration(context)
        if (game2Running == i || i == GAME2TIMER_ALL) {
            if (game2Running == i) {
                game2OnClockStopListener?.game2onClockStop(game2Running)
            } else {
                game2OnClockStopListener?.game2onClockStop(GAME2TIMER_ALL)
            }
            game2Running = GAME2NOT_RUNNING
            handler.removeCallbacksAndMessages(null)
        }
    }


    fun sync() {
        val timeNow = System.currentTimeMillis()
        Log.d("#####", "" + running)
        when (running) {
            TIMER1 -> {
                currentTime = timer1
            }

            TIMER2 -> {
                currentTime = timer2
            }

            TIMER3 -> {
                currentTime = timer3
            }

            TIMER4 -> {
                currentTime = timer4
            }
        }
        val clock = ((timeNow - timeThen) / 1000).toInt()
        if (clock > currentTime - startingTime + 3) {
            when (running) {
                TIMER1 -> {
                    timer1 = clock + startingTime
                }

                TIMER2 -> {
                    timer2 = clock + startingTime
                }

                TIMER3 -> {
                    timer3 = clock + startingTime
                }

                TIMER4 -> {
                    timer4 = clock + startingTime
                }
            }
        }
    }

    fun syncCountDown() {
        Log.d("#####", "" + game2Running)
        when (game2Running) {
            GAME2TIMER1 -> {
                runningTime = GAME2TIMER1
            }

            GAME2TIMER2 -> {
                runningTime = GAME2TIMER2
            }

            GAME2TIMER3 -> {
                runningTime = GAME2TIMER3
            }

            GAME2TIMER4 -> {
                runningTime = GAME2TIMER4
            }
        }
    }
}
