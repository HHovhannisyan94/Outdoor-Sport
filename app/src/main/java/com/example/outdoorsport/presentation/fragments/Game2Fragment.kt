package com.example.outdoorsport.presentation.fragments

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.outdoorsport.NetworkConnection
import com.example.outdoorsport.R
import com.example.outdoorsport.data.DataStoreHelper
import com.example.outdoorsport.data.Game2LogsFirebase
import com.example.outdoorsport.data.interfaces.CallBackInterface
import com.example.outdoorsport.data.model.Game2Logs
import com.example.outdoorsport.databinding.FragmentGame2Binding
import com.example.outdoorsport.presentation.viemodel.Game2LogsViewModel
import com.example.outdoorsport.utils.CountDownTimerService
import com.example.outdoorsport.utils.StopWatchInstance
import com.example.outdoorsport.utils.StopWatchInstance.mTimeLeftInMillis1
import com.example.outdoorsport.utils.StopWatchInstance.mTimeLeftInMillis2
import com.example.outdoorsport.utils.StopWatchInstance.mTimeLeftInMillis3
import com.example.outdoorsport.utils.StopWatchInstance.mTimeLeftInMillis4
import com.example.outdoorsport.utils.Util
import com.example.outdoorsport.utils.toast
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class Game2Fragment : Fragment(), CallBackInterface {

    lateinit var binding: FragmentGame2Binding
    private lateinit var notificationManager: NotificationManager
    private val viewModel: Game2LogsViewModel by activityViewModels()
    private lateinit var selectedMinute: String
    private lateinit var dataStoreHelper: DataStoreHelper
    private val loginFragment = LoginFragment.newInstance()
    private lateinit var sdf: SimpleDateFormat
    private lateinit var serviceIntent: Intent
    private lateinit var gameLogs: String

    private var isBackPressClicked = false
    private var isAdminBtnClicked = false

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGame2Binding.inflate(inflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        loginFragment.setCallBackInterface(this)
        sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm:ss", Locale.getDefault())
        notificationManager =
            requireActivity().getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        StopWatchInstance.initMediaPlayer(requireContext())

        StopWatchInstance.loadStateCodeGeneration(
            requireContext(),
            binding.timerTV1,
            binding.timerTV2,
            binding.timerTV3,
            binding.timerTV4
        )
        clickListeners()
        dataStoreHelper = DataStoreHelper(requireContext())

        lifecycleScope.launch {
            dataStoreHelper.minuteGame2Flow.collect { min ->
                selectedMinute = min
                mTimeLeftInMillis1 = selectedMinute.toLong().times(60000)
                mTimeLeftInMillis2 = selectedMinute.toLong().times(60000)
                mTimeLeftInMillis3 = selectedMinute.toLong().times(60000)
                mTimeLeftInMillis4 = selectedMinute.toLong().times(60000)

                with(binding) {
                    updateCountDownText(timerTV1, mTimeLeftInMillis1)
                    updateCountDownText(timerTV2, mTimeLeftInMillis2)
                    updateCountDownText(timerTV3, mTimeLeftInMillis3)
                    updateCountDownText(timerTV4, mTimeLeftInMillis4)
                }
            }
        }

        lifecycleScope.launch {
            dataStoreHelper.gameLogsFlow.collect { result ->
                gameLogs = result
            }
        }
        binding.btnAdminView.setOnClickListener {
            toast { "Long press to open Admin panel." }
        }

        binding.btnAdminView.setOnLongClickListener {
            loginFragment.show(requireActivity().supportFragmentManager, "LoginFragment")
            isAdminBtnClicked = true
            isBackPressClicked = false
            true
        }

        checkInternetConnection()
        serviceIntent = Intent(requireActivity(), CountDownTimerService::class.java)

        return binding.root
    }


    private fun clickListeners() {

        StopWatchInstance.setGame2ClockTickedListener { time, running ->
            binding.apply {
                when (running) {
                    StopWatchInstance.GAME2TIMER1 -> {
                        timerTV1.text = time
                    }

                    StopWatchInstance.GAME2TIMER2 -> {
                        timerTV2.text = time
                    }

                    StopWatchInstance.GAME2TIMER3 -> {
                        timerTV3.text = time
                    }

                    StopWatchInstance.GAME2TIMER4 -> {
                        timerTV4.text = time
                    }
                }
            }
        }

        binding.btnPauseView.setOnClickListener {
            context?.toast { "Long press to pause." }
        }

        binding.btnPauseView.setOnLongClickListener {
            toast { "Pause" }
            when (StopWatchInstance.game2Running) {
                StopWatchInstance.GAME2TIMER1 -> {
                    if (mTimerRunning1) {
                        pauseTimer1()
                        isRedPauseClicked = true
                    }

                    viewModel.addGame2Log(
                        Game2Logs(
                            0, "Red", StopWatchInstance.getCountDownTime(mTimeLeftInMillis1),
                            "The Red Team paused!" + " TIME: " + sdf.format(Date()),
                            redTeamCodes[countCodesRed]
                        )
                    )

                    viewModel.addGame2Data(
                        requireContext(), Game2LogsFirebase(
                            StopWatchInstance.getCountDownTime(mTimeLeftInMillis1),
                            "The Red Clock 1 paused!", redTeamCodes[countCodesRed]
                        ), gameLogs
                    )
                }

                StopWatchInstance.GAME2TIMER2 -> {
                    if (mTimerRunning2) {
                        pauseTimer2()
                        isBluePauseClicked = true
                    }

                    viewModel.addGame2Log(
                        Game2Logs(
                            0,
                            "Blue",
                            StopWatchInstance.getCountDownTime(mTimeLeftInMillis2),
                            "The Blue Team paused! " + " TIME: " + sdf.format(Date()),
                            blueTeamCodes[countCodesBlue]
                        )
                    )
                    viewModel.addGame2Data(
                        requireContext(), Game2LogsFirebase(
                            StopWatchInstance.getCountDownTime(mTimeLeftInMillis2),
                            "The Blue Clock 2 paused!", blueTeamCodes[countCodesBlue]
                        ), gameLogs
                    )
                }

                StopWatchInstance.GAME2TIMER3 -> {
                    if (mTimerRunning3) {
                        pauseTimer3()
                        isYellowPauseClicked = true
                    }

                    viewModel.addGame2Log(
                        Game2Logs(
                            0,
                            "Yellow",
                            StopWatchInstance.getCountDownTime(mTimeLeftInMillis3),
                            "The Yellow Team paused!" + " TIME: " + sdf.format(Date()),
                            yellowTeamCodes[countCodesYellow]
                        )
                    )
                    viewModel.addGame2Data(
                        requireContext(), Game2LogsFirebase(
                            StopWatchInstance.getCountDownTime(mTimeLeftInMillis3),
                            "The Yellow Team paused!", yellowTeamCodes[countCodesYellow]
                        ), gameLogs
                    )
                }

                StopWatchInstance.GAME2TIMER4 -> {
                    if (mTimerRunning4) {
                        pauseTimer4()
                        isGreenPauseClicked = true
                    }

                    viewModel.addGame2Log(
                        Game2Logs(
                            0,
                            "Green",
                            StopWatchInstance.getCountDownTime(mTimeLeftInMillis4),
                            "The Green Team paused!" + " TIME: " + sdf.format(Date()),
                            greenTeamCodes[countCodesGreen]
                        )
                    )
                    viewModel.addGame2Data(
                        requireContext(), Game2LogsFirebase(
                            StopWatchInstance.getCountDownTime(mTimeLeftInMillis4),
                            "The Green Team paused!", greenTeamCodes[countCodesGreen]
                        ), gameLogs
                    )
                }
            }
            StopWatchInstance.stopCountDownTimer(requireContext())
            true
        }

        binding.mcvTimer1.setOnClickListener {
            if (!mTimerRunning1) {
                if (countCodesRed < redTeamCodes.size) {
                    if (isRedPaused) {
                        resumeRedCDT()
                        StopWatchInstance.startCountDownTimer(StopWatchInstance.GAME2TIMER1)
                        mTimerRunning1 = true
                    } else {
                        resetTimer1(binding.timerTV1)
                        startCountDownTimer1()
                        StopWatchInstance.startCountDownTimer(StopWatchInstance.GAME2TIMER1)
                    }
                } else {
                    context?.toast { "No more codes for you" }
                    if (redTeamCodes.isNotEmpty()) {
                        redTeamCodes.clear()
                        countCodesRed = 0
                        Game2SettingsFragment.isTheCodeForAll.clear()
                        allTeamCodeIndex = 0
                        allTeamCodes.clear()
                    }
                }
            }

            if (mTimerRunning2 || isBluePauseClicked) {
                resetTimer2(binding.timerTV2)
                pauseTimer2()
                if (isBluePauseClicked) {
                    isBluePauseClicked = false
                }
            } else if (mTimerRunning3 || isYellowPauseClicked) {
                resetTimer3(binding.timerTV3)
                pauseTimer3()
                if (isYellowPauseClicked) {
                    isYellowPauseClicked = false
                }
            } else if (mTimerRunning4 || isGreenPauseClicked) {
                resetTimer4(binding.timerTV4)
                pauseTimer4()
                if (isGreenPauseClicked) {
                    isGreenPauseClicked = false
                }
            }
        }

        binding.mcvTimer2.setOnClickListener {
            if (!mTimerRunning2) {
                if (countCodesBlue < blueTeamCodes.size) {
                    if (isBluePaused) {
                        resumeBlueCDT()
                        StopWatchInstance.startCountDownTimer(StopWatchInstance.GAME2TIMER2)
                        mTimerRunning2 = true
                    } else {
                        resetTimer2(binding.timerTV2)
                        startCountDownTimer2()
                        StopWatchInstance.startCountDownTimer(StopWatchInstance.GAME2TIMER2)
                    }
                } else {
                    context?.toast { "No more codes for you" }
                    if (blueTeamCodes.isNotEmpty()) {
                        blueTeamCodes.clear()
                        countCodesBlue = 0
                        Game2SettingsFragment.isTheCodeForAll.clear()
                        allTeamCodeIndex = 0
                        allTeamCodes.clear()
                    }
                }
            }
            if (mTimerRunning1 || isRedPauseClicked) {
                resetTimer1(binding.timerTV1)
                pauseTimer1()
                if (isRedPauseClicked) {
                    isRedPauseClicked = false
                }
            } else if (mTimerRunning3 || isYellowPauseClicked) {
                resetTimer3(binding.timerTV3)
                pauseTimer3()
                if (isYellowPauseClicked) {
                    isYellowPauseClicked = false
                }
            } else if (mTimerRunning4 || isGreenPauseClicked) {
                resetTimer4(binding.timerTV4)
                pauseTimer4()
                if (isGreenPauseClicked) {
                    isGreenPauseClicked = false
                }
            }
        }

        binding.mcvTimer3.setOnClickListener {
            if (!mTimerRunning3) {
                if (countCodesYellow < yellowTeamCodes.size) {
                    if (isYellowPaused) {
                        resumeYellowCDT()
                        StopWatchInstance.startCountDownTimer(StopWatchInstance.GAME2TIMER3)
                        mTimerRunning3 = true
                    } else {
                        resetTimer3(binding.timerTV3)
                        startCountDownTimer3()
                        StopWatchInstance.startCountDownTimer(StopWatchInstance.GAME2TIMER3)
                    }
                } else {
                    context?.toast { "No more codes for you" }
                    if (yellowTeamCodes.isNotEmpty()) {
                        yellowTeamCodes.clear()
                        countCodesYellow = 0
                        Game2SettingsFragment.isTheCodeForAll.clear()
                        allTeamCodeIndex = 0
                        allTeamCodes.clear()

                    }
                }
            }

            if (mTimerRunning1 || isRedPauseClicked) {
                resetTimer1(binding.timerTV1)
                pauseTimer1()
                if (isRedPauseClicked) {
                    isRedPauseClicked = false
                }
            } else if (mTimerRunning2 || isBluePauseClicked) {
                resetTimer2(binding.timerTV2)
                pauseTimer2()
                if (isBluePauseClicked) {
                    isBluePauseClicked = false
                }
            } else if (mTimerRunning4 || isGreenPauseClicked) {
                resetTimer4(binding.timerTV4)
                pauseTimer4()
                if (isGreenPauseClicked) {
                    isGreenPauseClicked = false
                }
            }
        }

        binding.mcvTimer4.setOnClickListener {
            if (!mTimerRunning4) {
                if (countCodesGreen < greenTeamCodes.size) {
                    if (isGreenPaused) {
                        resumeGreenCDT()
                        StopWatchInstance.startCountDownTimer(StopWatchInstance.GAME2TIMER4)
                        mTimerRunning4 = true
                    } else {
                        resetTimer4(binding.timerTV4)
                        startCountDownTimer4()
                        StopWatchInstance.startCountDownTimer(StopWatchInstance.GAME2TIMER4)
                    }
                } else {
                    context?.toast { "No more codes for you" }
                    if (greenTeamCodes.isNotEmpty()) {
                        greenTeamCodes.clear()
                        countCodesGreen = 0
                        Game2SettingsFragment.isTheCodeForAll.clear()
                        allTeamCodeIndex = 0
                        allTeamCodes.clear()
                    }


                }
            }

            if (mTimerRunning1 || isRedPauseClicked) {
                resetTimer1(binding.timerTV1)
                pauseTimer1()
                if (isRedPauseClicked) {
                    isRedPauseClicked = false
                }
            } else if (mTimerRunning3 || isYellowPauseClicked) {
                resetTimer3(binding.timerTV3)
                pauseTimer3()
                if (isYellowPauseClicked) {
                    isYellowPauseClicked = false
                }
            } else if (mTimerRunning2 || isBluePauseClicked) {
                resetTimer2(binding.timerTV2)
                pauseTimer2()
                if (isBluePauseClicked) {
                    isBluePauseClicked = false
                }

            }
        }
    }

    override fun onPause() {
        super.onPause()
        println("Game2Fragment onPause()")

        if (StopWatchInstance.game2Running != StopWatchInstance.GAME2NOT_RUNNING) {
            requireActivity().unregisterReceiver(broadcastReceiver)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(requireActivity(), serviceIntent)
            } else {
                requireActivity().startService(serviceIntent)
            }

            StopWatchInstance.setGame2StopListener { running: Int ->
                //requireActivity().stopService(serviceIntent)
                notificationManager.cancel(2)
                StopWatchInstance.setGame2StopListener(null)
                when (running) {
                    StopWatchInstance.GAME2TIMER1 -> {
                        binding.timerTV1.text = StopWatchInstance.getCountDownTime(
                            mTimeLeftInMillis1
                        )

                        StopWatchInstance.resetCountDown(
                            StopWatchInstance.GAME2TIMER2,
                            requireContext()
                        )
                    }

                    StopWatchInstance.GAME2TIMER2 -> {
                        binding.timerTV2.text = StopWatchInstance.getCountDownTime(
                            mTimeLeftInMillis2
                        )

                        StopWatchInstance.resetCountDown(
                            StopWatchInstance.TIMER2,
                            requireContext()
                        )
                    }

                    StopWatchInstance.GAME2TIMER3 -> {
                        binding.timerTV3.text = StopWatchInstance.getCountDownTime(
                            mTimeLeftInMillis3
                        )

                        StopWatchInstance.resetCountDown(
                            StopWatchInstance.TIMER3,
                            requireContext()
                        )
                    }

                    StopWatchInstance.GAME2TIMER4 -> {
                        binding.timerTV4.text = StopWatchInstance.getCountDownTime(
                            mTimeLeftInMillis4
                        )

                        StopWatchInstance.resetCountDown(
                            StopWatchInstance.TIMER4,
                            requireContext()
                        )
                    }

                    StopWatchInstance.GAME2TIMER_ALL -> {
                        binding.timerTV1.text = StopWatchInstance.getCountDownTime(
                            mTimeLeftInMillis1
                        )
                        binding.timerTV2.text = StopWatchInstance.getCountDownTime(
                            mTimeLeftInMillis2
                        )
                        binding.timerTV3.text = StopWatchInstance.getCountDownTime(
                            mTimeLeftInMillis3
                        )
                        binding.timerTV4.text = StopWatchInstance.getCountDownTime(
                            mTimeLeftInMillis4
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter(CountDownTimerService.COUNTDOWN_BR)
        )
        binding.apply {

            if (CountDownTimerService.isRunning) {
                StopWatchInstance.syncCountDown()
                timerTV1.text = StopWatchInstance.getCountDownTime(mTimeLeftInMillis1)
                timerTV2.text = StopWatchInstance.getCountDownTime(mTimeLeftInMillis2)
                timerTV3.text = StopWatchInstance.getCountDownTime(mTimeLeftInMillis3)
                timerTV4.text = StopWatchInstance.getCountDownTime(mTimeLeftInMillis4)
            }

            StopWatchInstance.setGame2ClockTickedListener { time, running ->
                when (running) {
                    StopWatchInstance.GAME2TIMER1 -> {
                        timerTV1.text = time
                    }

                    StopWatchInstance.GAME2TIMER2 -> {
                        timerTV2.text = time
                    }

                    StopWatchInstance.GAME2TIMER3 -> {
                        timerTV3.text = time
                    }

                    StopWatchInstance.GAME2TIMER4 -> {
                        timerTV4.text = time
                    }
                }
            }
        }

        requireView().apply {
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    loginFragment.show(requireActivity().supportFragmentManager, "LoginFragment")
                    isBackPressClicked = true
                    isAdminBtnClicked = false
                    true
                } else false
            }
        }
    }

    private fun startCountDownTimer1() {
        countDownTimer1 = object : CountDownTimer(mTimeLeftInMillis1, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isRedPaused) {
                    cancel()
                } else {
                    mTimeLeftInMillis1 = millisUntilFinished
                    updateCountDownText(binding.timerTV1, mTimeLeftInMillis1)
                    handleStartTimer(mTimeLeftInMillis1)
                    mTimerRunning1 = true
                }
            }

            override fun onFinish() {
                mTimerRunning1 = false
                viewModel.addGame2Log(
                    Game2Logs(
                        0,
                        "Red",
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 60000),
                        "The Red Team discovered the code!" + " TIME: " + sdf.format(Date()),
                        redTeamCodes[countCodesRed]
                    )
                )

                viewModel.addGame2Data(
                    requireContext(), Game2LogsFirebase(
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 60000),
                        "The Red Team discovered the code!", redTeamCodes[countCodesRed]
                    ), gameLogs
                )
                showCode("Red")
                resetTimer1(binding.timerTV1)
            }
        }.start()
    }

    private fun pauseTimer1() {
        if (mTimerRunning1) {
            countDownTimer1.cancel()
            mTimerRunning1 = false
            isRedPaused = true
        }
        handleCancelTimer()
    }

    private fun startCountDownTimer2() {
        countDownTimer2 = object : CountDownTimer(mTimeLeftInMillis2, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isBluePaused) {
                    cancel()
                } else {
                    mTimeLeftInMillis2 = millisUntilFinished
                    updateCountDownText(binding.timerTV2, mTimeLeftInMillis2)
                    handleStartTimer(mTimeLeftInMillis2)
                    mTimerRunning2 = true
                }
            }

            override fun onFinish() {
                mTimerRunning2 = false

                viewModel.addGame2Log(
                    Game2Logs(
                        0,
                        "Blue",
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 60000),
                        "The Blue Team discovered the code!" + " TIME: " + sdf.format(Date()),
                        blueTeamCodes[countCodesBlue]
                    )
                )

                viewModel.addGame2Data(
                    requireContext(), Game2LogsFirebase(
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 60000),
                        "The Blue Team discovered the code!", blueTeamCodes[countCodesBlue]
                    ), gameLogs
                )
                showCode("Blue")
                resetTimer2(binding.timerTV2)
            }
        }.start()
    }

    private fun pauseTimer2() {
        if (mTimerRunning2) {
            countDownTimer2.cancel()
            mTimerRunning2 = false
            isBluePaused = true
        }
        handleCancelTimer()
    }

    private fun startCountDownTimer3() {
        countDownTimer3 = object : CountDownTimer(mTimeLeftInMillis3, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isYellowPaused) {
                    cancel()
                } else {
                    mTimeLeftInMillis3 = millisUntilFinished
                    updateCountDownText(binding.timerTV3, mTimeLeftInMillis3)
                    handleStartTimer(mTimeLeftInMillis3)
                    mTimerRunning3 = true
                }
            }

            override fun onFinish() {
                mTimerRunning3 = false

                viewModel.addGame2Log(
                    Game2Logs(
                        0,
                        "Yellow",
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 60000),
                        "The Yellow Team discovered the code!" + " TIME: " + sdf.format(Date()),
                        yellowTeamCodes[countCodesYellow]
                    )
                )

                viewModel.addGame2Data(
                    requireContext(), Game2LogsFirebase(
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 60000),
                        "The Yellow Team discovered the code!",
                        yellowTeamCodes[countCodesYellow]
                    ), gameLogs
                )
                showCode("Yellow")
                resetTimer3(binding.timerTV3)
            }
        }.start()
    }

    private fun pauseTimer3() {
        if (mTimerRunning3) {
            countDownTimer3.cancel()
            mTimerRunning3 = false
            isYellowPaused = true
        }
        handleCancelTimer()
    }

    private fun startCountDownTimer4() {
        countDownTimer4 = object : CountDownTimer(mTimeLeftInMillis4, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isGreenPaused) {
                    cancel()
                } else {
                    mTimeLeftInMillis4 = millisUntilFinished
                    updateCountDownText(binding.timerTV4, mTimeLeftInMillis4)
                    handleStartTimer(mTimeLeftInMillis4)
                    mTimerRunning4 = true
                }
            }

            override fun onFinish() {
                mTimerRunning4 = false

                viewModel.addGame2Log(
                    Game2Logs(
                        0,
                        "Green",
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 60000),
                        "The Green Team discovered the code!" + " TIME: " + sdf.format(Date()),
                        greenTeamCodes[countCodesGreen]
                    )
                )

                viewModel.addGame2Data(
                    requireContext(), Game2LogsFirebase(
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 60000),
                        "The Green Team discovered the code!", greenTeamCodes[countCodesGreen]
                    ), gameLogs
                )
                showCode("Green")
                resetTimer4(binding.timerTV4)
            }
        }.start()
    }

    private fun pauseTimer4() {
        if (mTimerRunning4) {
            countDownTimer4.cancel()
            mTimerRunning4 = false
            isGreenPaused = true
        }
        handleCancelTimer()
    }


    private fun updateCountDownText(textView: TextView, mTimeLeftInMillis: Long) {
        val f: NumberFormat = DecimalFormat("00")
        //   val hour = mTimeLeftInMillis / 3600000 % 24
        val min = mTimeLeftInMillis / 60000 % 60
        val sec = mTimeLeftInMillis / 1000 % 60
        val time = f.format(min) + ":" + f.format(sec)
        textView.text = time
    }


    private fun resetTimer1(textView: TextView) {
        mTimeLeftInMillis1 = selectedMinute.toLong() * 60000
        updateCountDownText(textView, selectedMinute.toLong() * 60000)
    }

    private fun resetTimer2(textView: TextView) {
        mTimeLeftInMillis2 = selectedMinute.toLong() * 60000
        updateCountDownText(textView, selectedMinute.toLong() * 60000)
    }

    private fun resetTimer3(textView: TextView) {
        mTimeLeftInMillis3 = selectedMinute.toLong() * 60000
        updateCountDownText(textView, selectedMinute.toLong() * 60000)
    }

    private fun resetTimer4(textView: TextView) {
        mTimeLeftInMillis4 = selectedMinute.toLong() * 60000
        updateCountDownText(textView, selectedMinute.toLong() * 60000)
    }


    private fun checkInternetConnection() {
        val checkNetworkConnection = NetworkConnection(requireActivity())
        checkNetworkConnection.observe(viewLifecycleOwner) { isConnected: Boolean ->
            val shapeAppearanceModel = ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, android.R.attr.radius.toFloat())
                .build()
            val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)

            ViewCompat.setBackground(binding.txtCodeGeneration, shapeDrawable)
            context?.let { context ->
                if (isConnected) {
                    binding.txtCodeGeneration.text = buildString {
                        append("Code Generation (Online)")
                    }
                    shapeDrawable.fillColor =
                        ContextCompat.getColorStateList(context, R.color.green)
                    shapeDrawable.setStroke(
                        5.0f,
                        ContextCompat.getColor(context, R.color.dark_green)
                    )
                } else {
                    binding.txtCodeGeneration.text = buildString {
                        append("Code Generation (Offline)")
                    }
                    shapeDrawable.fillColor = ContextCompat.getColorStateList(context, R.color.red)
                    shapeDrawable.setStroke(5.0f, ContextCompat.getColor(context, R.color.dark_red))
                }
            }
        }
    }

    fun showCode(selectedTeam: String) {
        val displayCodesFragment = DisplayCodesFragment.newInstance()
        val args = Bundle()
        args.putString("selectedTeam", selectedTeam)
        displayCodesFragment.arguments = args
        displayCodesFragment.show(
            requireActivity().supportFragmentManager,
            "DisplayCodesFragment"
        )

        when (selectedTeam) {
            "Red" -> {
                countCodesRed++
            }

            "Blue" -> {
                countCodesBlue++
            }

            "Yellow" -> {
                countCodesYellow++
            }

            "Green" -> {
                countCodesGreen++
            }

        }
        allTeamCodeIndex++
        StopWatchInstance.stopCountDownTimer(requireContext())
        //DisplayCodesFragment.isDialogVisible = true
    }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            with(binding) {
                updateCountDownText(timerTV1, mTimeLeftInMillis1)
                updateCountDownText(timerTV2, mTimeLeftInMillis2)
                updateCountDownText(timerTV3, mTimeLeftInMillis3)
                updateCountDownText(timerTV4, mTimeLeftInMillis4)
            }
        }
    }

    fun handleStartTimer(mTimeLeftInMillis: Long) {
        CountDownTimerService.milliSec = mTimeLeftInMillis
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // ServiceCompat.stopForeground(CountDownTimerService(), ServiceCompat.STOP_FOREGROUND_DETACH)
            ContextCompat.startForegroundService(requireActivity(), serviceIntent)
        } else {
            requireActivity().startService(serviceIntent)
        }
        println("timerStarted")
    }


    private fun handleCancelTimer() {
        requireActivity().stopService(serviceIntent)
    }


    private fun resumeRedCDT() {
        val millisInFuture: Long = mTimeLeftInMillis1
        isRedPaused = false

        object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isRedPaused) {
                    cancel()
                } else {
                    mTimeLeftInMillis1 = millisUntilFinished
                    updateCountDownText(binding.timerTV1, millisUntilFinished)
                    handleStartTimer(mTimeLeftInMillis1)
                    mTimerRunning1 = true
                }
            }

            override fun onFinish() {
                mTimerRunning1 = false

                viewModel.addGame2Log(
                    Game2Logs(
                        0,
                        "Red",
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 1000),
                        "The Red Team discovered the code!" + " TIME: " + sdf.format(Date()),
                        redTeamCodes[countCodesRed]
                    )
                )
                viewModel.addGame2Data(
                    requireContext(), Game2LogsFirebase(
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 1000),
                        "The Red Team  discovered the code!", redTeamCodes[countCodesRed]
                    ), gameLogs
                )
                showCode("Red")
                resetTimer1(binding.timerTV1)
            }
        }.start()
    }


    private fun resumeBlueCDT() {
        val millisInFuture: Long = mTimeLeftInMillis2
        isBluePaused = false

        object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isBluePaused) {
                    cancel()
                } else {
                    mTimeLeftInMillis2 = millisUntilFinished
                    updateCountDownText(binding.timerTV2, millisUntilFinished)
                    handleStartTimer(mTimeLeftInMillis2)
                    mTimerRunning2 = true
                }
            }

            override fun onFinish() {
                mTimerRunning2 = false

                viewModel.addGame2Log(
                    Game2Logs(
                        0,
                        "Blue",
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 1000),
                        "The Blue Team discovered the code!" + " TIME: " + sdf.format(Date()),
                        blueTeamCodes[countCodesBlue]
                    )
                )
                viewModel.addGame2Data(
                    requireContext(), Game2LogsFirebase(
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 1000),
                        "The Blue Team discovered the code!", blueTeamCodes[countCodesBlue]
                    ), gameLogs
                )

                showCode("Blue")
                resetTimer2(binding.timerTV2)
            }
        }.start()
    }


    private fun resumeYellowCDT() {
        val millisInFuture: Long = mTimeLeftInMillis3
        isYellowPaused = false

        object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isYellowPaused) {
                    cancel()
                } else {
                    mTimeLeftInMillis3 = millisUntilFinished
                    updateCountDownText(binding.timerTV3, millisUntilFinished)
                    handleStartTimer(mTimeLeftInMillis3)
                    mTimerRunning3 = true
                }
            }

            override fun onFinish() {
                mTimerRunning3 = false
                viewModel.addGame2Log(
                    Game2Logs(
                        0,
                        "Yellow",
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 1000),
                        "The Yellow Team discovered the code!" + " TIME: " + sdf.format(Date()),
                        yellowTeamCodes[countCodesYellow]
                    )
                )
                viewModel.addGame2Data(
                    requireContext(), Game2LogsFirebase(
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 1000),
                        "The Yellow Team discovered the code!", yellowTeamCodes[countCodesYellow]
                    ), gameLogs
                )
                showCode("Yellow")
                resetTimer3(binding.timerTV3)
            }
        }.start()
    }


    private fun resumeGreenCDT() {
        val millisInFuture: Long = mTimeLeftInMillis4
        isGreenPaused = false

        object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isGreenPaused) {
                    cancel()
                } else {
                    mTimeLeftInMillis4 = millisUntilFinished
                    updateCountDownText(binding.timerTV4, mTimeLeftInMillis4)
                    handleStartTimer(mTimeLeftInMillis4)
                    mTimerRunning4 = true
                }
            }

            override fun onFinish() {
                mTimerRunning4 = false
                viewModel.addGame2Log(
                    Game2Logs(
                        0,
                        "Green",
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 1000),
                        "The Green Team discovered the code!" + " TIME: " + sdf.format(Date()),
                        greenTeamCodes[countCodesGreen]
                    )
                )

                viewModel.addGame2Data(
                    requireContext(), Game2LogsFirebase(
                        StopWatchInstance.getCountDownTime(selectedMinute.toLong() * 1000),
                        "The Green Team discovered the code!", greenTeamCodes[countCodesGreen]
                    ), gameLogs
                )
                showCode("Green")
                resetTimer4(binding.timerTV4)
            }
        }.start()
    }

    override fun onStop() {
        try {
            requireActivity().unregisterReceiver(broadcastReceiver)
        } catch (e: Exception) {
            // Receiver was probably already stopped in onPause()
            println(e.message)
        }
        super.onStop()
    }


    companion object {
        fun newInstance() = Game2Fragment()
        var redTeamCodes = mutableListOf<String>()
        var blueTeamCodes = mutableListOf<String>()
        var yellowTeamCodes = mutableListOf<String>()
        var greenTeamCodes = mutableListOf<String>()

        var allTeamCodes = mutableListOf<String>()
        var allTeamCodeIndex = 0

        var countCodesRed = 0
        var countCodesBlue = 0
        var countCodesYellow = 0
        var countCodesGreen = 0

        private var mTimerRunning1 = false
        private var mTimerRunning2 = false
        private var mTimerRunning3 = false
        private var mTimerRunning4 = false

        private var isRedPaused = false
        private var isBluePaused = false
        private var isYellowPaused = false
        private var isGreenPaused = false

        private var isRedPauseClicked = false
        private var isBluePauseClicked = false
        private var isYellowPauseClicked = false
        private var isGreenPauseClicked = false

        private lateinit var countDownTimer1: CountDownTimer
        private lateinit var countDownTimer2: CountDownTimer
        private lateinit var countDownTimer3: CountDownTimer
        private lateinit var countDownTimer4: CountDownTimer
    }

    override fun callBackMethod() {
        if (isBackPressClicked) {
            Util.loadFragment(requireActivity(), GameChoiceFragment.newInstance())
        }
        if (isAdminBtnClicked) {
            Util.loadFragment(requireActivity(), Game2SettingsFragment.newInstance())
        }
    }
}