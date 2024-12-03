package com.example.outdoorsport.presentation.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.outdoorsport.R
import com.example.outdoorsport.data.DataStoreHelper
import com.example.outdoorsport.data.model.TeamCodes
import com.example.outdoorsport.databinding.FragmentGame2SettingsBinding
import com.example.outdoorsport.presentation.viemodel.TeamCodesViewModel
import com.example.outdoorsport.utils.Util
import com.example.outdoorsport.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Game2SettingsFragment : Fragment() {
    lateinit var binding: FragmentGame2SettingsBinding
    private lateinit var dataStoreHelper: DataStoreHelper
    private val viewModel: TeamCodesViewModel by activityViewModels()
    private var selectedTeam = ""
    private var radioBtnMinute = 0
    private var radioBtnSeconds = 0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = FragmentGame2SettingsBinding.inflate(inflater, container, false)
        binding.apply {
            radioGroup3.clearCheck()
        }
        dataStoreHelper = DataStoreHelper(requireContext())

        when (radioBtnMinute) {
            R.id.radio_btn_5 -> {
                binding.radioBtn5.isChecked = true
            }

            R.id.radio_btn_15 -> {
                binding.radioBtn15.isChecked = true
            }

            R.id.radio_btn_30 -> {
                binding.radioBtn30.isChecked = true
            }

            R.id.radio_btn_60 -> {
                binding.radioBtn60.isChecked = true
            }
        }

        when (radioBtnSeconds) {
            R.id.radio_btn_15_sec -> {
                binding.radioBtn15Sec.isChecked = true
            }

            R.id.radio_btn_30_sec -> {
                binding.radioBtn30Sec.isChecked = true
            }
        }


        val checkedChangeListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val checkedButton = group.findViewById<RadioButton>(checkedId)
            when (checkedId) {
                R.id.radio_btn_5 -> {
                    radioBtnMinute = R.id.radio_btn_5
                }

                R.id.radio_btn_15 -> {
                    radioBtnMinute = R.id.radio_btn_15
                }

                R.id.radio_btn_30 -> {
                    radioBtnMinute = R.id.radio_btn_30
                }

                R.id.radio_btn_60 -> {
                    radioBtnMinute = R.id.radio_btn_60
                }
            }
            lifecycleScope.launch {
                dataStoreHelper.storeMinuteGame2(checkedButton?.text.toString())
            }
        }


        with(binding) {
            radioGroup1.setOnCheckedChangeListener(checkedChangeListener)
            radioGroup2CheckedListener()
            radioGroup3CheckedListener()
            btnLogs.setOnClickListener {
                Util.loadFragment(requireActivity(), Game2LogsFragment.newInstance())
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    radioGroup3.visibility = View.GONE
                    radioGroup3.clearCheck()
                    linearLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        )
                    )
                    btnAdd.backgroundTintList = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.black
                    )
                } else {
                    radioGroup3.visibility = View.VISIBLE
                }
            }

            btnAdd.setOnClickListener {
                if (isCodeChecked()) {
                    val selectedId: Int = radioGroup3.checkedRadioButtonId
                    val radioButton3 = radioGroup3.findViewById<View>(selectedId) as? RadioButton
                    val selectedRbText: String = radioButton3?.text.toString()
                    if (selectedId != -1) {
                        when (selectedRbText) {
                            "1.R" -> {
                                selectedTeam = "Red"
                                Game2Fragment.redTeamCodes.add(edtxtCode.text.toString())
                                Game2Fragment.countCodesRed = 0
                                isTheCodeForAll.add(false)
                            }

                            "2.B" -> {
                                selectedTeam = "Blue"
                                Game2Fragment.blueTeamCodes.add(edtxtCode.text.toString())
                                Game2Fragment.countCodesBlue = 0
                                isTheCodeForAll.add(false)
                            }

                            "3.Y" -> {
                                selectedTeam = "Yellow"
                                Game2Fragment.yellowTeamCodes.add(edtxtCode.text.toString())
                                Game2Fragment.countCodesYellow = 0
                                isTheCodeForAll.add(false)
                            }

                            "4.G" -> {
                                selectedTeam = "Green"
                                Game2Fragment.greenTeamCodes.add(edtxtCode.text.toString())
                                Game2Fragment.countCodesGreen = 0
                            }
                        }
                        isTheCodeForAll.add(false)
                        viewModel.addTeamCode(
                            TeamCodes(
                                0,
                                selectedTeam,
                                "Code: " + edtxtCode.text.toString()
                            )
                        )
                        context?.toast { "Entered code for $selectedTeam" }
                    } else {
                        with(Game2Fragment) {
                            redTeamCodes.add(edtxtCode.text.toString())
                            blueTeamCodes.add(edtxtCode.text.toString())
                            yellowTeamCodes.add(edtxtCode.text.toString())
                            greenTeamCodes.add(edtxtCode.text.toString())
                            allTeamCodes.add(edtxtCode.text.toString())

                            countCodesRed = 0
                            countCodesBlue = 0
                            countCodesYellow = 0
                            countCodesGreen = 0
                            isTheCodeForAll.add(true)
                        }

                        viewModel.addTeamCode(
                            TeamCodes(
                                0,
                                "All of Teams",
                                "Code: " + edtxtCode.text.toString()
                            )
                        )
                        context?.toast { "You entered the code for all teams" }
                    }
                    Game2Fragment.allTeamCodeIndex = 0

                    edtxtCode.text?.clear()
                    radioGroup3.clearCheck()
                    linearLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.gray
                        )
                    )
                    btnAdd.backgroundTintList = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.black
                    )
                }
            }


            btnClose.setOnClickListener {
                Util.loadFragment(
                    requireActivity(),
                    AdminPanelFragment.newInstance()
                )
            }

            btnCodes.setOnClickListener{
                CodeListFragment().show(
                    requireActivity().supportFragmentManager,
                    "CodeListFragment"
                )
            }

            fragmentGame2Container.viewTreeObserver
                .addOnGlobalLayoutListener {
                    val r = Rect()
                    fragmentGame2Container.getWindowVisibleDisplayFrame(r)
                    val screenHeight: Int = binding.fragmentGame2Container.rootView.height
                    val keypadHeight = screenHeight - r.bottom
                    if (keypadHeight > screenHeight * 0.15) {
                        txtChooseTime.visibility = View.GONE
                        txtChooseSecond.visibility = View.GONE
                        radioGroup1.visibility = View.GONE
                        radioGroup2.visibility = View.GONE
                    } else {
                        txtChooseTime.visibility = View.VISIBLE
                        txtChooseSecond.visibility = View.VISIBLE
                        radioGroup1.visibility = View.VISIBLE
                        radioGroup2.visibility = View.VISIBLE
                    }
                }

        }
        return binding.root
    }

    private fun radioGroup2CheckedListener() {
        with(binding) {
            radioGroup2.setOnCheckedChangeListener { group2, checkedId2 ->
                val radioButton2 = group2
                    .findViewById<View>(checkedId2) as RadioButton
                lifecycleScope.launch {
                    dataStoreHelper.storeSecondGame2(radioButton2.text as String)
                }
                when (checkedId2) {
                    R.id.radio_btn_15_sec -> {
                        radioBtnSeconds = R.id.radio_btn_15_sec
                    }

                    R.id.radio_btn_30_sec -> {
                        radioBtnSeconds = R.id.radio_btn_30_sec
                    }
                }
            }
        }
    }

    private fun radioGroup3CheckedListener() {
        with(binding) {
            radioGroup3.setOnCheckedChangeListener { group3, checkedId3 ->
                val radioButton3 = group3.findViewById<View>(checkedId3) as? RadioButton

                when (radioButton3?.text) {
                    "1.R" -> {
                        linearLayout.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.light_red
                            )
                        )
                        btnAdd.backgroundTintList = ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.dark_red
                        )
                    }

                    "2.B" -> {
                        linearLayout.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.light_blue
                            )
                        )
                        btnAdd.backgroundTintList = ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.blue
                        )
                    }

                    "3.Y" -> {
                        linearLayout.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.light_yellow
                            )
                        )
                        btnAdd.backgroundTintList = ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.dark_yellow
                        )
                    }

                    "4.G" -> {
                        linearLayout.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.light_green
                            )
                        )
                        btnAdd.backgroundTintList = ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.dark_green
                        )
                    }
                }
            }
        }
    }

    private fun isCodeChecked(): Boolean {
        if (binding.edtxtCode.toString().trim()
                .isEmpty() || TextUtils.isEmpty(binding.edtxtCode.text.toString())
        ) {
            binding.edtxtCode.error = "This field is required"
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                Util.loadFragment(requireActivity(), AdminPanelFragment.newInstance())
                true
            } else false
        }
    }

    companion object {
        fun newInstance() = Game2SettingsFragment()
        var isTheCodeForAll = mutableListOf<Boolean>()
    }
}