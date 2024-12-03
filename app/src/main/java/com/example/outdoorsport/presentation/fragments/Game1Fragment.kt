package com.example.outdoorsport.presentation.fragments

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.outdoorsport.NetworkConnection
import com.example.outdoorsport.R
import com.example.outdoorsport.data.DataStoreHelper
import com.example.outdoorsport.data.Game1LogsFirebase
import com.example.outdoorsport.data.interfaces.CallBackInterface
import com.example.outdoorsport.data.model.Game1Logs
import com.example.outdoorsport.databinding.FragmentGame1Binding
import com.example.outdoorsport.presentation.viemodel.Game1LogsViewModel
import com.example.outdoorsport.utils.StopWatchInstance
import com.example.outdoorsport.utils.TimerService
import com.example.outdoorsport.utils.Util
import com.example.outdoorsport.utils.toast
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class Game1Fragment : Fragment(), CallBackInterface {
    private lateinit var notificationManager: NotificationManager
    private val loginFragment = LoginFragment.newInstance()
    lateinit var binding: FragmentGame1Binding
    private val viewModel: Game1LogsViewModel by activityViewModels()
    private lateinit var sdf: SimpleDateFormat
    private lateinit var serviceIntent: Intent
    private lateinit var dataStoreHelper: DataStoreHelper
    private var isBackPressClicked = false
    private var isAdminBtnClicked = false

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGame1Binding.inflate(inflater, container, false)
        loginFragment.setCallBackInterface(this)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm:ss", Locale.getDefault())
        dataStoreHelper = DataStoreHelper(requireContext())

        notificationManager =
            requireActivity().getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        StopWatchInstance.initMediaPlayer(requireContext())
        StopWatchInstance.loadState(
            requireContext(),
            binding.timerTV1,
            binding.timerTV2,
            binding.timerTV3,
            binding.timerTV4
        )

        binding.btnAdminView.setOnClickListener {
            toast { "Long press to open Admin panel." }
        }

        binding.btnAdminView.setOnLongClickListener {
            loginFragment.show(requireActivity().supportFragmentManager, "LoginFragment")
            isAdminBtnClicked = true
            isBackPressClicked = false
            true
        }

        lifecycleScope.launch {
            dataStoreHelper.gameLogsFlow.collect { result ->
                clickListeners(result)
            }
        }
        checkInternetConnection()
        serviceIntent = Intent(requireActivity(), TimerService::class.java)

        with(binding) {
            timerTV1.text = StopWatchInstance.getTime(StopWatchInstance.timer1)
            timerTV2.text = StopWatchInstance.getTime(StopWatchInstance.timer2)
            timerTV3.text = StopWatchInstance.getTime(StopWatchInstance.timer3)
            timerTV4.text = StopWatchInstance.getTime(StopWatchInstance.timer4)
        }

        return binding.root
    }


    private fun clickListeners(gameLogs: String) {

        StopWatchInstance.setClockTickedListener { time, running ->
            when (running) {
                StopWatchInstance.TIMER1 -> {
                    binding.timerTV1.text = time
                }

                StopWatchInstance.TIMER2 -> {
                    binding.timerTV2.text = time
                }

                StopWatchInstance.TIMER3 -> {
                    binding.timerTV3.text = time
                }

                StopWatchInstance.TIMER4 -> {
                    binding.timerTV4.text = time
                }
            }
        }

        binding.btnPauseView.setOnClickListener {
            toast {
                "Long press to pause."
            }
        }

        binding.btnPauseView.setOnLongClickListener {
            when (StopWatchInstance.running) {
                StopWatchInstance.TIMER1 -> {

                    viewModel.addGame1Log(
                        Game1Logs(
                            0,
                            "Red",
                            StopWatchInstance.getTime(StopWatchInstance.timer1),
                            sdf.format(Date()),
                            "The Red Clock paused! " + StopWatchInstance.getTime(
                                StopWatchInstance.timer1
                            )
                        )
                    )

                    viewModel.addGame1Data(
                        requireContext(), Game1LogsFirebase(
                            StopWatchInstance.getTime(StopWatchInstance.timer1),
                            "The Red Clock paused! "
                        ), gameLogs
                    )
                }

                StopWatchInstance.TIMER2 -> {

                    viewModel.addGame1Log(
                        Game1Logs(
                            0,
                            "Blue",
                            StopWatchInstance.getTime(StopWatchInstance.timer2),
                            sdf.format(Date()),
                            "The Blue Clock paused! " + StopWatchInstance.getTime(
                                StopWatchInstance.timer2
                            )
                        )
                    )

                    viewModel.addGame1Data(
                        requireContext(), Game1LogsFirebase(
                            StopWatchInstance.getTime(StopWatchInstance.timer2),
                            "The Blue Clock paused! "
                        ), gameLogs
                    )
                }

                StopWatchInstance.TIMER3 -> {

                    viewModel.addGame1Log(
                        Game1Logs(
                            0,
                            "Yellow",
                            StopWatchInstance.getTime(StopWatchInstance.timer3),
                            sdf.format(Date()),
                            "The Yellow Clock paused! " + StopWatchInstance.getTime(
                                StopWatchInstance.timer3
                            )
                        )
                    )

                    viewModel.addGame1Data(
                        requireContext(), Game1LogsFirebase(
                            StopWatchInstance.getTime(StopWatchInstance.timer3),
                            "The Yellow Clock paused! "
                        ), gameLogs
                    )
                }

                StopWatchInstance.TIMER4 -> {
                    viewModel.addGame1Log(
                        Game1Logs(
                            0,
                            "Green",
                            StopWatchInstance.getTime(StopWatchInstance.timer4),
                            sdf.format(Date()),
                            "The Green Clock paused! " + StopWatchInstance.getTime(
                                StopWatchInstance.timer4
                            )
                        )
                    )

                    viewModel.addGame1Data(
                        requireContext(), Game1LogsFirebase(
                            StopWatchInstance.getTime(StopWatchInstance.timer4),
                            "The Green Clock paused! "
                        ), gameLogs
                    )
                }
            }
            //  lifecycleScope.launch {
            StopWatchInstance.stopTimer(requireContext())
            // }
            true
        }

        binding.mcvTimer1.setOnClickListener {
            StopWatchInstance.startTimer(StopWatchInstance.TIMER1)
            viewModel.addGame1Log(
                Game1Logs(
                    0,
                    "Red",
                    StopWatchInstance.getTime(StopWatchInstance.timer1),
                    sdf.format(Date()),
                    "The Red Clock is running!"
                            + ", \nBlue:" + StopWatchInstance.getTime(StopWatchInstance.timer2)
                            + ", \nYellow:" + StopWatchInstance.getTime(StopWatchInstance.timer3)
                            + ", \nGreen:" + StopWatchInstance.getTime(StopWatchInstance.timer4)
                )
            )
            viewModel.addGame1Data(
                requireContext(), Game1LogsFirebase(
                    StopWatchInstance.getTime(StopWatchInstance.timer1),
                    "The Red Clock is running!"
                            + ", Blue:" + StopWatchInstance.getTime(StopWatchInstance.timer2)
                            + ", Yellow:" + StopWatchInstance.getTime(StopWatchInstance.timer3)
                            + ", Green:" + StopWatchInstance.getTime(StopWatchInstance.timer4)
                ), gameLogs
            )
        }

        binding.mcvTimer2.setOnClickListener {
            StopWatchInstance.startTimer(StopWatchInstance.TIMER2)

            viewModel.addGame1Log(
                Game1Logs(
                    0,
                    "Blue",
                    StopWatchInstance.getTime(StopWatchInstance.timer2),
                    sdf.format(Date()),
                    "The Blue Clock is running!"
                            + ", \nRed:" + StopWatchInstance.getTime(StopWatchInstance.timer1)
                            + ", \nYellow:" + StopWatchInstance.getTime(StopWatchInstance.timer3)
                            + ", \nGreen:" + StopWatchInstance.getTime(StopWatchInstance.timer4)
                )
            )

            viewModel.addGame1Data(
                requireContext(), Game1LogsFirebase(
                    StopWatchInstance.getTime(StopWatchInstance.timer2),
                    "The Blue Clock is running!"
                            + ", Red:" + StopWatchInstance.getTime(StopWatchInstance.timer1)
                            + ", Yellow:" + StopWatchInstance.getTime(StopWatchInstance.timer3)
                            + ", Green:" + StopWatchInstance.getTime(StopWatchInstance.timer4)
                ), gameLogs
            )
        }

        binding.mcvTimer3.setOnClickListener {
            StopWatchInstance.startTimer(StopWatchInstance.TIMER3)

            viewModel.addGame1Log(
                Game1Logs(
                    0,
                    "Yellow",
                    StopWatchInstance.getTime(StopWatchInstance.timer3),
                    sdf.format(Date()),
                    " The Yellow is running!"
                            + ", \nRed:" + StopWatchInstance.getTime(StopWatchInstance.timer1)
                            + ", \nBlue:" + StopWatchInstance.getTime(StopWatchInstance.timer2)
                            + ", \nGreen:" + StopWatchInstance.getTime(StopWatchInstance.timer4)
                )
            )

            viewModel.addGame1Data(
                requireContext(), Game1LogsFirebase(
                    StopWatchInstance.getTime(StopWatchInstance.timer3),
                    "The Yellow is running!"
                            + ", Red:" + StopWatchInstance.getTime(StopWatchInstance.timer1)
                            + ", Blue:" + StopWatchInstance.getTime(StopWatchInstance.timer2)
                            + ", Green:" + StopWatchInstance.getTime(StopWatchInstance.timer4)
                ), gameLogs
            )
        }

        binding.mcvTimer4.setOnClickListener {
            StopWatchInstance.startTimer(StopWatchInstance.TIMER4)

            viewModel.addGame1Log(
                Game1Logs(
                    0,
                    "Green",
                    StopWatchInstance.getTime(StopWatchInstance.timer4),
                    sdf.format(Date()),
                    "The Green Clock is running!"
                            + ", \nRed:" + StopWatchInstance.getTime(StopWatchInstance.timer1)
                            + ", \nBlue:" + StopWatchInstance.getTime(StopWatchInstance.timer2)
                            + ", \nYellow:" + StopWatchInstance.getTime(StopWatchInstance.timer3)
                )
            )

            viewModel.addGame1Data(
                requireContext(), Game1LogsFirebase(
                    StopWatchInstance.getTime(StopWatchInstance.timer4),
                    "The Green Clock is running!"
                            + ", Red:" + StopWatchInstance.getTime(StopWatchInstance.timer1)
                            + ", Blue:" + StopWatchInstance.getTime(StopWatchInstance.timer2)
                            + ", Yellow:" + StopWatchInstance.getTime(StopWatchInstance.timer3)
                ), gameLogs
            )
        }
    }


    override fun onPause() {
        super.onPause()
        //StopWatchInstance.saveState(requireContext())
        if (StopWatchInstance.running != StopWatchInstance.NOT_RUNNING) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireActivity().startForegroundService(serviceIntent)
            } else {
                requireActivity().startService(serviceIntent)
            }
            StopWatchInstance.setStopListener { running: Int ->
                requireActivity().stopService(serviceIntent)
                Handler(Looper.getMainLooper()).postDelayed({
                    notificationManager.cancel(1)
                    StopWatchInstance.setStopListener(null)
                    when (running) {
                        StopWatchInstance.TIMER1 -> {
                            binding.timerTV1.text = getString(R.string.initial)
                            StopWatchInstance.reset(StopWatchInstance.TIMER1, requireContext())

                        }

                        StopWatchInstance.TIMER2 -> {
                            binding.timerTV2.text = getString(R.string.initial)
                            //  lifecycleScope.launch {
                            StopWatchInstance.reset(StopWatchInstance.TIMER2, requireContext())
                            //  }
                        }

                        StopWatchInstance.TIMER3 -> {
                            binding.timerTV3.text = getString(R.string.initial)
                            StopWatchInstance.reset(StopWatchInstance.TIMER3, requireContext())

                        }

                        StopWatchInstance.TIMER4 -> {
                            binding.timerTV4.text = getString(R.string.initial)
                            StopWatchInstance.reset(StopWatchInstance.TIMER4, requireContext())

                        }

                        StopWatchInstance.TIMER_ALL -> {
                            binding.timerTV1.text = getString(R.string.initial)
                            binding.timerTV2.text = getString(R.string.initial)
                            binding.timerTV3.text = getString(R.string.initial)
                            binding.timerTV4.text = getString(R.string.initial)
                        }
                    }
                }, 100)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (TimerService.isRunning) {
            StopWatchInstance.sync()
            binding.timerTV1.text = StopWatchInstance.getTime(StopWatchInstance.timer1)
            binding.timerTV2.text = StopWatchInstance.getTime(StopWatchInstance.timer2)
            binding.timerTV3.text = StopWatchInstance.getTime(StopWatchInstance.timer3)
            binding.timerTV4.text = StopWatchInstance.getTime(StopWatchInstance.timer4)
        }

        StopWatchInstance.setClockTickedListener { time, running ->
            when (running) {
                StopWatchInstance.TIMER1 -> {
                    binding.timerTV1.text = time
                }

                StopWatchInstance.TIMER2 -> {
                    binding.timerTV2.text = time
                }

                StopWatchInstance.TIMER3 -> {
                    binding.timerTV3.text = time
                }

                StopWatchInstance.TIMER4 -> {
                    binding.timerTV4.text = time
                }
            }
        }

        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                loginFragment.show(requireActivity().supportFragmentManager, "LoginFragment")
                isBackPressClicked = true
                isAdminBtnClicked = false
                true
            } else false
        }
    }

    private fun checkInternetConnection() {
        val checkNetworkConnection = NetworkConnection(requireActivity())
        checkNetworkConnection.observe(viewLifecycleOwner) { isConnected: Boolean ->
            val shapeAppearanceModel = ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, android.R.attr.radius.toFloat())
                .build()
            val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)

            ViewCompat.setBackground(binding.txtClocks, shapeDrawable)
            context?.let {
                if (isConnected) {
                    binding.txtClocks.text = buildString {
                        append("Clocks (Online)")
                    }
                    shapeDrawable.fillColor = ContextCompat.getColorStateList(it, R.color.green)
                    shapeDrawable.setStroke(5.0f, ContextCompat.getColor(it, R.color.dark_green))
                } else {
                    binding.txtClocks.text = buildString {
                        append("Clocks (Offline)")
                    }
                    shapeDrawable.fillColor = ContextCompat.getColorStateList(it, R.color.red)
                    shapeDrawable.setStroke(5.0f, ContextCompat.getColor(it, R.color.dark_red))
                }
            }
        }
    }

    companion object {
        fun newInstance() = Game1Fragment()
    }

    override fun callBackMethod() {
        if (isBackPressClicked) {
            Util.loadFragment(requireActivity(), GameChoiceFragment.newInstance())
        }
        if (isAdminBtnClicked) {
            Util.loadFragment(requireActivity(), AdminClocksFragment())
        }
    }
}