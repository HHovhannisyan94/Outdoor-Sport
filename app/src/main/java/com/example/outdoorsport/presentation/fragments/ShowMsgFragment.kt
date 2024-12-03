package com.example.outdoorsport.presentation.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.outdoorsport.R
import com.example.outdoorsport.data.DataStoreHelper
import com.example.outdoorsport.databinding.FragmentShowMsgBinding
import com.example.outdoorsport.utils.StopWatchInstance
import com.example.outdoorsport.utils.Util
import kotlinx.coroutines.launch


class ShowMsgFragment : DialogFragment() {
    lateinit var binding: FragmentShowMsgBinding
    private lateinit var dataStoreHelper: DataStoreHelper

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentShowMsgBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        dataStoreHelper = DataStoreHelper(requireContext())

        val prev = requireActivity().supportFragmentManager.findFragmentByTag("ShowMsgFragment")
        StopWatchInstance.initMediaPlayer(requireContext())
        StopWatchInstance.mediaPlayer?.start()

        lifecycleScope.launch {
            dataStoreHelper.secondGame3Flow.collect { sec ->
                Handler(Looper.getMainLooper()).postDelayed({
                    if (prev != null && prev is ShowMsgFragment) {
                        prev.dismissAllowingStateLoss()
                    }
                }, sec.toLong() * 1000)
            }
        }


        binding.run {
            val mArgs = arguments
            val isInvalidCode = mArgs?.getBoolean("isInvalid")
            when (mArgs?.getString("selectedTeam")) {
                "Red" -> {
                    if (isInvalidCode == true) {
                        showMsgError(R.color.dark_red)
                    } else {
                        with(Game3Fragment) {
                            showMsg(
                                redTeamCodes,
                                redTeamMsgs,
                                indexCodesRed,
                                R.color.dark_red
                            )
                        }
                    }
                }

                "Blue" -> {
                    if (isInvalidCode == true) {
                        showMsgError(R.color.blue)
                    } else {
                        with(Game3Fragment) {
                            showMsg(
                                blueTeamCodes,
                                blueTeamMsgs,
                                indexCodesBlue,
                                R.color.blue
                            )
                        }
                    }
                }

                "Yellow" -> {
                    if (isInvalidCode == true) {
                        showMsgError(R.color.dark_yellow)
                    } else {
                        with(Game3Fragment) {
                            showMsg(
                                yellowTeamCodes,
                                yellowTeamMsgs,
                                indexCodesYellow,
                                R.color.dark_yellow
                            )

                        }
                    }
                }

                "Green" -> {
                    if (isInvalidCode == true) {
                        showMsgError(R.color.dark_green)
                    } else {
                        with(Game3Fragment) {
                            showMsg(
                                greenTeamCodes,
                                greenTeamMsgs,
                                indexCodesGreen,
                                R.color.dark_green
                            )

                        }
                    }
                }


            }
        }
        return binding.root
    }

    private fun showMsg(
        teamCodes: MutableList<String>,
        teamMsgs: MutableList<String>,
        index: Int,
        color: Int
    ) {
        binding.cardView.strokeColor = ContextCompat.getColor(
            requireContext(),
            color
        )
        if (Game3Fragment.isCodeMsgForAll) {
            binding.tvMsg.text =
                Util.changeTextStyle(
                    "Message:\n " + Game3Fragment.allTeamMsgs[Game3Fragment.allTeamCodeMsgIndex],
                    ContextCompat.getColor(
                        requireContext(),
                        color
                    )
                )
            Game3Fragment.allTeamCodes.remove(Game3Fragment.allTeamCodes[Game3Fragment.allTeamCodeMsgIndex])
            Game3Fragment.allTeamMsgs.remove(Game3Fragment.allTeamMsgs[Game3Fragment.allTeamCodeMsgIndex])
        } else {
            binding.tvMsg.text = Util.changeTextStyle(
                "Message:\n " + teamMsgs[index],
                ContextCompat.getColor(
                    requireContext(),
                    color
                )
            )
            teamCodes.remove(teamCodes[index])
            teamMsgs.remove(teamMsgs[index])
        }
    }


    private fun showMsgError(color: Int) {
        binding.cardView.strokeColor = ContextCompat.getColor(
            requireContext(),
            color
        )
        binding.tvMsg.text = Util.changeTextStyle(
            "Invalid code",
            ContextCompat.getColor(
                requireContext(),
                color
            )
        )
    }

    override fun getTheme() = R.style.RoundedCornersDialog

    override fun onStart() {
        super.onStart()
        val letterCount = binding.tvMsg.text.toString().length
        val height = (resources.displayMetrics.heightPixels * 0.90).toInt()
        val textHeight = (resources.displayMetrics.heightPixels)
        val width = (resources.displayMetrics.widthPixels)
        if (letterCount > 150) {
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            binding.cardView.minimumWidth = textHeight
            binding.cardView.minimumHeight = width
            binding.tvMsg.width = textHeight
            binding.tvMsg.height = width
        } else {
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                height
            )
        }

    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Log.d("ABSDIALOGFRAG", "Exception", e)
        }
    }
}