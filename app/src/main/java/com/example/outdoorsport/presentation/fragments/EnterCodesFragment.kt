package com.example.outdoorsport.presentation.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.outdoorsport.R
import com.example.outdoorsport.data.DataStoreHelper
import com.example.outdoorsport.data.Game3LogsFirebase
import com.example.outdoorsport.data.model.Game3Logs
import com.example.outdoorsport.databinding.FragmentEnterCodeBinding
import com.example.outdoorsport.presentation.fragments.Game3Fragment.Companion.allTeamCodes
import com.example.outdoorsport.presentation.fragments.Game3Fragment.Companion.blueTeamMsgs
import com.example.outdoorsport.presentation.fragments.Game3Fragment.Companion.greenTeamMsgs
import com.example.outdoorsport.presentation.fragments.Game3Fragment.Companion.indexCodesBlue
import com.example.outdoorsport.presentation.fragments.Game3Fragment.Companion.indexCodesGreen
import com.example.outdoorsport.presentation.fragments.Game3Fragment.Companion.indexCodesRed
import com.example.outdoorsport.presentation.fragments.Game3Fragment.Companion.indexCodesYellow
import com.example.outdoorsport.presentation.fragments.Game3Fragment.Companion.isCodeMsgForAll
import com.example.outdoorsport.presentation.fragments.Game3Fragment.Companion.redTeamMsgs
import com.example.outdoorsport.presentation.fragments.Game3Fragment.Companion.yellowTeamMsgs
import com.example.outdoorsport.presentation.viemodel.Game3LogsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EnterCodesFragment : DialogFragment() {
    private lateinit var binding: FragmentEnterCodeBinding
    private lateinit var dataStoreHelper: DataStoreHelper
    private val viewModel by viewModels<Game3LogsViewModel>(ownerProducer = { requireActivity() })
    private lateinit var countDownTimer: CountDownTimer
    var isRunning = false
    private lateinit var gameLogs: String
    var index = 0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        dataStoreHelper = DataStoreHelper(requireContext())

        lifecycleScope.launch {
            dataStoreHelper.gameLogsFlow.collect { result ->
                gameLogs = result
            }
        }

        binding.run {
            tvClose.setOnClickListener {
                edtxtCode.text?.clear()

                if (isRunning) {
                    countDownTimer.cancel()
                }
                dismiss()
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            btnCheck.setOnClickListener {
                val mArgs = arguments

                when (val selectedTeam = mArgs?.getString("selectedTeam")) {
                    "Red" -> {
                        showMsg(
                            selectedTeam,
                            Game3Fragment.redTeamCodes
                        )
                    }

                    "Blue" -> {
                        showMsg(
                            selectedTeam,
                            Game3Fragment.blueTeamCodes
                        )
                    }

                    "Yellow" -> {
                        showMsg(
                            selectedTeam,
                            Game3Fragment.yellowTeamCodes
                        )
                    }

                    "Green" -> {
                        showMsg(
                            selectedTeam,
                            Game3Fragment.greenTeamCodes
                        )
                    }
                }
                edtxtCode.text?.clear()
            }
        }
        return binding.root
    }


    private fun isCodeChecked(): Boolean {
        with(binding) {
            if (edtxtCode.toString().trim().isEmpty() || TextUtils.isEmpty(edtxtCode.text.toString())) {
                edtxtCode.error = "This field is required"
                return false
            }
        }
        return true
    }

    private fun showMsg(selectedTeam: String, teamCodes: MutableList<String>) {
        lifecycleScope.launch {
            dataStoreHelper.minuteGame3Flow.collect { min ->
                binding.apply {
                    when (selectedTeam) {
                        "Red" -> {
                            countDown.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.dark_red
                                )
                            )
                        }

                        "Blue" -> {
                            countDown.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.blue
                                )
                            )
                        }

                        "Yellow" -> {
                            countDown.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.dark_yellow
                                )
                            )
                        }

                        "Green" -> {
                            countDown.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.dark_green
                                )
                            )
                        }
                    }

                    if (isCodeChecked()) {
                        lyEnterCode.visibility = View.GONE
                        countDown.visibility = View.VISIBLE

                        isRunning = true

                        val etText = edtxtCode.text.toString()

                        val prev = requireActivity().supportFragmentManager.findFragmentByTag("EnterCodesFragment")
                        countDownTimer = object : CountDownTimer(min.toLong() * 60000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                val f: NumberFormat = DecimalFormat("00")
                                val minute = (millisUntilFinished / 60000) % 60
                                val sec = (millisUntilFinished / 1000) % 60
                                val time = f.format(minute) + ":" + f.format(sec)
                                countDown.text = time
                            }

                            @SuppressLint("SourceLockedOrientationActivity")
                            override fun onFinish() {
                                when (selectedTeam) {
                                    "Red" -> {
                                        checkEnteredCodes(
                                            teamCodes,
                                            redTeamMsgs,
                                            selectedTeam,
                                            etText
                                        )
                                        indexCodesRed = index
                                    }

                                    "Blue" -> {
                                        checkEnteredCodes(
                                            teamCodes,
                                            blueTeamMsgs,
                                            selectedTeam,
                                            etText
                                        )
                                        indexCodesBlue = index
                                    }

                                    "Yellow" -> {
                                        checkEnteredCodes(
                                            teamCodes,
                                            yellowTeamMsgs,
                                            selectedTeam,
                                            etText
                                        )
                                        indexCodesYellow = index
                                    }

                                    "Green" -> {
                                        checkEnteredCodes(
                                            teamCodes,
                                            greenTeamMsgs,
                                            selectedTeam,
                                            etText
                                        )
                                        indexCodesGreen = index
                                    }
                                }
                                isRunning = false
                                binding.countDown.visibility = View.GONE
                                if (prev != null && prev is EnterCodesFragment) {
                                    prev.dismissAllowingStateLoss()
                                }
                                requireActivity().requestedOrientation =
                                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            }
                        }.start()
                    }
                }
            }
        }
    }

    private fun checkEnteredCodes(
        teamCodes: MutableList<String>,
        teamMsgs: MutableList<String>,
        selectedTeam: String,
        enteredCode: String) {
        val sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm:ss", Locale.getDefault())
        val currentDateAndTime: String = ", TIME: " + sdf.format(Date())
        val showMsgFragment = ShowMsgFragment()
        val args = Bundle()
        args.putString("selectedTeam", selectedTeam)
        args.putString("enteredCode", enteredCode)
        showMsgFragment.arguments = args

        val validCodeString = "$selectedTeam Team entered valid code"
        val invalidCodeString =
            "$selectedTeam Team entered invalid code:$enteredCode"
        if (allTeamCodes.isNotEmpty() && allTeamCodes.contains(enteredCode)) {
            isCodeMsgForAll = true
            for (i in allTeamCodes.indices) {
                if (allTeamCodes[i] == enteredCode) {
                    Game3Fragment.allTeamCodeMsgIndex = i
                    break
                }
            }

            isRunning = false

            viewModel.addGame3Data(
                requireContext(), Game3LogsFirebase(
                    validCodeString,
                    allTeamCodes[Game3Fragment.allTeamCodeMsgIndex],
                    Game3Fragment.allTeamMsgs[Game3Fragment.allTeamCodeMsgIndex]
                ), gameLogs
            )

            viewModel.addGame3Log(
                Game3Logs(
                    0,
                    selectedTeam,
                    validCodeString + currentDateAndTime,
                    allTeamCodes[Game3Fragment.allTeamCodeMsgIndex],
                    Game3Fragment.allTeamMsgs[Game3Fragment.allTeamCodeMsgIndex]
                )
            )

            showMsgFragment.show(
                requireActivity().supportFragmentManager,
                "ShowMsgFragment"
            )

            if (dialog?.isShowing == true) {
                dismiss()
            }

        } else if (teamCodes.isNotEmpty() && teamCodes.contains(enteredCode)) {
            for (i in teamCodes.indices) {
                if (teamCodes[i] == enteredCode) {
                    index = i
                    break
                }
            }

            isCodeMsgForAll = false

            showMsgFragment.show(
                requireActivity().supportFragmentManager,
                "ShowMsgFragment"
            )

            if (dialog?.isShowing == true) {
                dismiss()
            }

            isRunning = false

            viewModel.addGame3Data(
                requireContext(), Game3LogsFirebase(
                    validCodeString,
                    teamCodes[index],
                    teamMsgs[index]
                ), gameLogs
            )

            viewModel.addGame3Log(
                Game3Logs(
                    0,
                    selectedTeam,

                    "${invalidCodeString}${currentDateAndTime}",
                    teamCodes[index],
                    teamMsgs[index]
                )
            )
        } else {
            if (allTeamCodes.isNotEmpty() && !allTeamCodes.contains(enteredCode)) {
                viewModel.addGame3Data(
                    requireContext(), Game3LogsFirebase(
                        invalidCodeString,
                        "",
                        ""
                    ), gameLogs
                )
                viewModel.addGame3Log(
                    Game3Logs(
                        0,
                        selectedTeam,
                        "${invalidCodeString}${currentDateAndTime}",
                        "",
                        ""
                    )
                )
            } else if (teamCodes.isNotEmpty() && !teamCodes.contains(enteredCode)) {
                viewModel.addGame3Data(
                    requireContext(), Game3LogsFirebase(
                        invalidCodeString,
                        "",
                        ""
                    ), gameLogs
                )
                viewModel.addGame3Log(
                    Game3Logs(
                        0,
                        selectedTeam,

                        invalidCodeString,
                        "",
                        ""
                    )
                )
            }
            args.putBoolean("isInvalid", true)
            showMsgFragment.arguments = args
            showMsgFragment.show(
                requireActivity().supportFragmentManager,
                "ShowMsgFragment"
            )

        }
    }



    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.98).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.70).toInt()
        dialog?.window?.setLayout(width, height)

        val mArgs = arguments
        when (mArgs?.getString("selectedTeam")) {
            "Red" -> {

                binding.apply {
                    cardView.strokeColor = ContextCompat.getColor(
                        requireContext(),
                        R.color.dark_red
                    )
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_red
                        )
                    )
                    btnCheck.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.dark_red)
                    tvEnterCode.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_red
                        )
                    )
                }
            }

            "Blue" -> {
                binding.apply {
                    cardView.strokeColor = ContextCompat.getColor(
                        requireContext(),
                        R.color.blue
                    )
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )
                    btnCheck.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.blue)
                    tvEnterCode.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.blue
                        )
                    )
                }
            }

            "Yellow" -> {
                binding.apply {
                    cardView.strokeColor = ContextCompat.getColor(
                        requireContext(),
                        R.color.dark_yellow
                    )
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_yellow
                        )
                    )
                    btnCheck.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.dark_yellow)
                    tvEnterCode.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_yellow
                        )
                    )
                }
            }

            "Green" -> {
                binding.apply {
                    cardView.strokeColor = ContextCompat.getColor(
                        requireContext(),
                        R.color.dark_green
                    )
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_green
                        )
                    )
                    btnCheck.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.dark_green)
                    binding.tvEnterCode.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_green
                        )
                    )
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        requireView().apply {
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener { _, keyCode, event ->

                if (event.action == KeyEvent.ACTION_UP || event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss()
                    requireActivity().requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    true
                } else false
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitNowAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Log.d("ABSDIALOGFRAG", "Exception", e)
        }
    }

    override fun getTheme() = R.style.RoundedCornersDialog

    companion object {
        fun newInstance() = EnterCodesFragment()
    }
}