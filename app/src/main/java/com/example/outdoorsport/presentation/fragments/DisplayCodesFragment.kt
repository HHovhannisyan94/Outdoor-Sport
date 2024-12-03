package com.example.outdoorsport.presentation.fragments

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
import com.example.outdoorsport.databinding.FragmentDisplayCodeBinding
import com.example.outdoorsport.utils.StopWatchInstance
import com.example.outdoorsport.utils.Util
import kotlinx.coroutines.launch


class DisplayCodesFragment : DialogFragment() {
    lateinit var binding: FragmentDisplayCodeBinding
    private lateinit var dataStoreHelper: DataStoreHelper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDisplayCodeBinding.inflate(inflater, container, false)
        dataStoreHelper = DataStoreHelper(requireContext())
        StopWatchInstance.initMediaPlayer(requireContext())
        lifecycleScope.launch {
            dataStoreHelper.secondGame2Flow.collect { sec ->
                Handler(Looper.getMainLooper()).postDelayed({
                    if (dialog?.isShowing == true) {
                        dismiss()
                    }
                }, sec.toLong() * 1000)
            }
        }

        binding.apply {
            val mArgs = arguments
            when (mArgs?.getString("selectedTeam")) {
                "Red" -> {
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


                    with(Game2Fragment) {
                        tvShowCode.text = Util.changeTextStyle(
                            "The code:\n" + redTeamCodes[countCodesRed - 1],
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.dark_red
                            )
                        )
                        for (i in allTeamCodes.indices) {
                            if (allTeamCodes[i] == redTeamCodes[countCodesRed - 1]) {
                                if (blueTeamCodes.contains(redTeamCodes[countCodesRed - 1])
                                    && yellowTeamCodes.contains(redTeamCodes[countCodesRed - 1])
                                    && greenTeamCodes.contains(redTeamCodes[countCodesRed - 1])
                                ) {
                                    for (y in yellowTeamCodes.indices) {
                                        if (yellowTeamCodes[y] == redTeamCodes[countCodesRed - 1]) {
                                            yellowTeamCodes.remove(redTeamCodes[countCodesRed - 1])
                                            break
                                        }
                                    }

                                    for (b in blueTeamCodes.indices) {
                                        if (blueTeamCodes[b] == redTeamCodes[countCodesRed - 1]) {
                                            blueTeamCodes.remove(redTeamCodes[countCodesRed - 1])
                                            break
                                        }
                                    }

                                    for (g in greenTeamCodes.indices) {
                                        if (greenTeamCodes[g] == redTeamCodes[countCodesRed - 1]) {
                                            greenTeamCodes.remove(redTeamCodes[countCodesRed - 1])
                                            break
                                        }
                                    }
                                    redTeamCodes.removeAt(countCodesRed - 1)

                                    if (countCodesRed > 0) {
                                        countCodesRed--
                                    }

                                }
                                break
                            }
                        }
                    }
                }

                "Blue" -> {
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_blue
                        )
                    )

                    cardView.strokeColor = ContextCompat.getColor(
                        requireContext(),
                        R.color.blue
                    )


                    with(Game2Fragment) {

                        tvShowCode.text = Util.changeTextStyle(
                            "The code:\n" + blueTeamCodes[countCodesBlue - 1],
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.blue
                            )
                        )
                        for (i in allTeamCodes.indices) {
                            if (allTeamCodes[i] == blueTeamCodes[countCodesBlue - 1]) {
                                if (redTeamCodes.contains(blueTeamCodes[countCodesBlue - 1])
                                    && yellowTeamCodes.contains(blueTeamCodes[countCodesBlue - 1])
                                    && greenTeamCodes.contains(blueTeamCodes[countCodesBlue - 1])
                                ) {
                                    for (r in redTeamCodes.indices) {
                                        if (redTeamCodes[r] == blueTeamCodes[countCodesBlue - 1]) {
                                            redTeamCodes.remove(blueTeamCodes[countCodesBlue - 1])
                                            break
                                        }
                                    }

                                    for (y in yellowTeamCodes.indices) {
                                        if (yellowTeamCodes[y] == blueTeamCodes[countCodesBlue - 1]) {
                                            yellowTeamCodes.remove(blueTeamCodes[countCodesBlue - 1])
                                            break
                                        }
                                    }

                                    for (g in greenTeamCodes.indices) {
                                        if (greenTeamCodes[g] == blueTeamCodes[countCodesBlue - 1]) {
                                            greenTeamCodes.remove(blueTeamCodes[countCodesBlue - 1])
                                            break
                                        }
                                    }
                                    blueTeamCodes.removeAt(countCodesBlue - 1)

                                    if (countCodesBlue > 0) {
                                        countCodesBlue--
                                    }
                                }
                                break
                            }
                        }
                    }
                }

                "Yellow" -> {
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_yellow
                        )
                    )

                    cardView.strokeColor = ContextCompat.getColor(
                        requireContext(),
                        R.color.dark_yellow
                    )


                    with(Game2Fragment) {
                        tvShowCode.text = Util.changeTextStyle(
                            "The code:\n" + yellowTeamCodes[countCodesYellow - 1],
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.dark_yellow
                            )
                        )

                        for (i in allTeamCodes.indices) {
                            if (allTeamCodes[i] == yellowTeamCodes[countCodesYellow - 1]) {
                                if (redTeamCodes.contains(yellowTeamCodes[countCodesYellow - 1])
                                    && blueTeamCodes.contains(yellowTeamCodes[countCodesYellow - 1])
                                    && greenTeamCodes.contains(yellowTeamCodes[countCodesYellow - 1])
                                ) {
                                    for (r in redTeamCodes.indices) {
                                        if (redTeamCodes[r] == yellowTeamCodes[countCodesYellow - 1]) {
                                            redTeamCodes.remove(yellowTeamCodes[countCodesYellow - 1])
                                            break
                                        }
                                    }

                                    for (b in blueTeamCodes.indices) {
                                        if (blueTeamCodes[b] == yellowTeamCodes[countCodesYellow - 1]) {
                                            blueTeamCodes.remove(yellowTeamCodes[countCodesYellow - 1])
                                            break
                                        }
                                    }

                                    for (g in greenTeamCodes.indices) {
                                        if (greenTeamCodes[g] == yellowTeamCodes[countCodesYellow - 1]) {
                                            greenTeamCodes.remove(yellowTeamCodes[countCodesYellow - 1])
                                            break
                                        }
                                    }
                                    yellowTeamCodes.removeAt(countCodesYellow - 1)
                                    if (countCodesYellow > 0) {
                                        countCodesYellow--
                                    }

                                    break
                                }
                            }

                        }
                    }
                }

                "Green" -> {
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_green
                        )
                    )

                    cardView.strokeColor = ContextCompat.getColor(
                        requireContext(),
                        R.color.dark_green
                    )

                    with(Game2Fragment) {

                        tvShowCode.text = Util.changeTextStyle(
                            "The code:\n" + greenTeamCodes[countCodesGreen - 1],
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.dark_green
                            )
                        )
                        for (i in allTeamCodes.indices) {
                            if (allTeamCodes[i] == greenTeamCodes[countCodesGreen - 1]) {

                                if (redTeamCodes.contains(greenTeamCodes[countCodesGreen - 1])
                                    && blueTeamCodes.contains(greenTeamCodes[countCodesGreen - 1])
                                    && yellowTeamCodes.contains(greenTeamCodes[countCodesGreen - 1])
                                ) {
                                    for (r in redTeamCodes.indices) {
                                        if (redTeamCodes[r] == greenTeamCodes[countCodesGreen - 1]) {
                                            redTeamCodes.remove(greenTeamCodes[countCodesGreen - 1])
                                            break
                                        }
                                    }

                                    for (b in blueTeamCodes.indices) {
                                        if (blueTeamCodes[b] == greenTeamCodes[countCodesGreen - 1]) {
                                            blueTeamCodes.remove(greenTeamCodes[countCodesGreen - 1])
                                            break
                                        }
                                    }

                                    for (y in yellowTeamCodes.indices) {
                                        if (yellowTeamCodes[y] == greenTeamCodes[countCodesGreen - 1]) {
                                            yellowTeamCodes.remove(greenTeamCodes[countCodesGreen - 1])
                                            break
                                        }
                                    }

                                    greenTeamCodes.removeAt(countCodesGreen - 1)
                                    if (countCodesGreen > 0) {
                                        countCodesGreen--
                                    }
                                }

                                break
                            }

                        }
                    }
                }
            }
        }

        return binding.root
    }


    override fun onPause() {
        super.onPause()
        println("DisplayCodesFragment onPause()")
        if (dialog?.isShowing == true) {
            dismiss()
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

    override fun getTheme() = R.style.RoundedCornersDialog


    override fun onStart() {
        super.onStart()

        val letterCount = binding.tvShowCode.text.toString().length

        val textHeight = (resources.displayMetrics.heightPixels)
        val width = (resources.displayMetrics.widthPixels)

        val height = (resources.displayMetrics.heightPixels * 0.90).toInt()
        if (letterCount > 50) {
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            binding.cardView.minimumWidth = textHeight
            binding.cardView.minimumHeight = width
            binding.tvShowCode.width = textHeight
            binding.tvShowCode.height = width
        } else {
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                height
            )
        }
    }

    companion object {
        fun newInstance() = DisplayCodesFragment()
    }


}