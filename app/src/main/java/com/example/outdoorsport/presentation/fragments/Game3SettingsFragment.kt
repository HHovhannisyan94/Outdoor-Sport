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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.outdoorsport.R
import com.example.outdoorsport.data.DataStoreHelper
import com.example.outdoorsport.data.model.TeamCodeMsg
import com.example.outdoorsport.databinding.FragmentGame3SettingsBinding
import com.example.outdoorsport.presentation.CustomEditText
import com.example.outdoorsport.presentation.viemodel.TeamCodesMsgViewModel
import com.example.outdoorsport.utils.Util
import com.example.outdoorsport.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Game3SettingsFragment : Fragment() {
    lateinit var binding: FragmentGame3SettingsBinding
    private lateinit var dataStoreHelper: DataStoreHelper
    private var messages = mutableListOf<String>()
    private val viewModel: TeamCodesMsgViewModel by activityViewModels()
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
        binding = FragmentGame3SettingsBinding.inflate(inflater, container, false)
        dataStoreHelper = DataStoreHelper(requireContext())

        binding.apply {
            radioGroup3.clearCheck()
        }

        radioGroup1CheckedListener()
        radioGroup2CheckedListener()
        radioGroup3CheckedListener()

        with(binding) {

            when (radioBtnMinute) {
                R.id.radio_btn_1 -> {
                    radioBtn1.isChecked = true
                }

                R.id.radio_btn_5 -> {
                    radioBtn5.isChecked = true
                }

                R.id.radio_btn_10 -> {
                    radioBtn10.isChecked = true
                }

                R.id.radio_btn_30 -> {
                    radioBtn30.isChecked = true
                }
            }

            when (radioBtnSeconds) {
                R.id.radio_btn_30_sec -> {
                    radioBtn30Sec.isChecked = true
                }

                R.id.radio_btn_45_sec -> {
                    radioBtn45Sec.isChecked = true
                }
            }

            btnLogs.setOnClickListener {
                Util.loadFragment(
                    requireActivity(),
                    Game3LogsFragment.newInstance()
                )
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    println("isChecked")
                    radioGroup3.visibility = View.GONE
                    radioGroup3.clearCheck()
                    lyMsgCode.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.gray
                        )
                    )
                    btnAddMsgCode.backgroundTintList = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.black
                    )
                } else {
                    radioGroup3.visibility = View.VISIBLE
                }
            }

            btnAddMsgCode.setOnClickListener {
                if (isCodeChecked()) {
                    val selectedId: Int = radioGroup3.checkedRadioButtonId
                    val radioButton4 = radioGroup3
                        .findViewById<View>(selectedId) as? RadioButton
                    val selectedRbText: String = radioButton4?.text.toString()
                    if (selectedId != -1) {
                        when (selectedRbText) {
                            "1.R" -> {
                                selectedTeam = "Red"
                                Game3Fragment.redTeamMsgs.add(edtxtMsg.text.toString())
                                Game3Fragment.redTeamCodes.add(edtxtCode.text.toString())
                                Game3Fragment.indexCodesRed = 0
                                //allTeamCodeMsg.add(false)
                            }

                            "2.B" -> {
                                selectedTeam = "Blue"
                                Game3Fragment.blueTeamMsgs.add(edtxtMsg.text.toString())
                                Game3Fragment.blueTeamCodes.add(edtxtCode.text.toString())
                                Game3Fragment.indexCodesBlue = 0
                                // allTeamCodeMsg.add(false)
                            }

                            "3.Y" -> {
                                selectedTeam = "Yellow"
                                Game3Fragment.yellowTeamMsgs.add(edtxtMsg.text.toString())
                                Game3Fragment.yellowTeamCodes.add(edtxtCode.text.toString())
                                Game3Fragment.indexCodesYellow = 0
                                // allTeamCodeMsg.add(false)
                            }

                            "4.G" -> {
                                selectedTeam = "Green"
                                Game3Fragment.greenTeamMsgs.add(edtxtMsg.text.toString())
                                Game3Fragment.greenTeamCodes.add(edtxtCode.text.toString())
                                Game3Fragment.indexCodesGreen = 0
                                // allTeamCodeMsg.add(false)
                            }
                        }
                        messages.add(edtxtMsg.text.toString())
                        viewModel.addTeamCodeMsg(
                            TeamCodeMsg(
                                0,
                                selectedTeam,
                                "Code: " + edtxtCode.text.toString(),
                                "Message: " + edtxtMsg.text.toString()
                            )
                        )
                        toast { "Entered a message & a code for $selectedTeam" }
                    } else {
                        Game3Fragment.allTeamCodes.add(edtxtCode.text.toString())
                        Game3Fragment.allTeamMsgs.add(edtxtMsg.text.toString())
                        Game3Fragment.allTeamCodeMsgIndex = 0
                        toast { "You entered the code & the msg for all teams" }
                        viewModel.addTeamCodeMsg(
                            TeamCodeMsg(
                                0,
                                "All of Teams",
                                "Code: " + edtxtCode.text.toString(),
                                "Message: " + edtxtMsg.text.toString()
                            )
                        )
                        //allTeamCodeMsg.add(true)
                    }
                    edtxtCode.text?.clear()
                    edtxtMsg.text?.clear()
                    radioGroup3.clearCheck()
                    lyMsgCode.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.gray
                        )
                    )
                    btnAddMsgCode.backgroundTintList = ContextCompat.getColorStateList(
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
                messages.clear()
            }

            game3SettingsContainer.viewTreeObserver
                .addOnGlobalLayoutListener {
                    val r = Rect()
                    game3SettingsContainer.getWindowVisibleDisplayFrame(r)
                    val screenHeight: Int = game3SettingsContainer.rootView.height
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

            edtxtCode.setOnBackPressListener(object : CustomEditText.MyEditTextListener {
                override fun callback() {
                    backPress()
                }
            })

            edtxtMsg.setOnBackPressListener(object : CustomEditText.MyEditTextListener {
                override fun callback() {
                    backPress()
                }
            })

            btnCodesMsg.setOnClickListener {
                CodeMsgListFragment().show(
                    requireActivity().supportFragmentManager,
                    "CodeMsgListFragment"
                )
            }
        }

        return binding.root
    }


    private fun radioGroup1CheckedListener() {
        with(binding) {
            radioGroup1.setOnCheckedChangeListener { group1, checkedId1 ->
                val radioButton1 = group1
                    .findViewById<View>(checkedId1) as? RadioButton
                lifecycleScope.launch {
                    dataStoreHelper.storeMinuteGame3(radioButton1?.text as String)
                }

                when (checkedId1) {
                    R.id.radio_btn_1 -> {
                        radioBtnMinute = R.id.radio_btn_1
                    }

                    R.id.radio_btn_5 -> {
                        radioBtnMinute = R.id.radio_btn_5
                    }

                    R.id.radio_btn_10 -> {
                        radioBtnMinute = R.id.radio_btn_10
                    }

                    R.id.radio_btn_30 -> {
                        radioBtnMinute = R.id.radio_btn_30
                    }
                }

            }
        }
    }

    private fun radioGroup2CheckedListener() {
        with(binding) {
            radioGroup2.setOnCheckedChangeListener { group2, checkedId2 ->
                val radioButton2 = group2
                    .findViewById<View>(checkedId2) as? RadioButton
                lifecycleScope.launch {
                    dataStoreHelper.storeSecondGame3(radioButton2?.text as String)
                }
                when (checkedId2) {
                    R.id.radio_btn_30_sec -> {
                        radioBtnSeconds = R.id.radio_btn_30_sec
                    }

                    R.id.radio_btn_45_sec -> {
                        radioBtnSeconds = R.id.radio_btn_45_sec
                    }
                }
            }
        }
    }

    private fun radioGroup3CheckedListener() {
        with(binding) {
            radioGroup3.setOnCheckedChangeListener { group3, checkedId3 ->
                val radioButton3 = group3
                    .findViewById<View>(checkedId3) as? RadioButton

                println("radioButton3:  " + radioButton3?.text)
                when (radioButton3?.text) {
                    "1.R" -> {
                        lyMsgCode.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.light_red
                            )
                        )
                        btnAddMsgCode.backgroundTintList = ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.dark_red
                        )
                    }

                    "2.B" -> {
                        lyMsgCode.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.light_blue
                            )
                        )
                        btnAddMsgCode.backgroundTintList = ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.blue
                        )
                    }

                    "3.Y" -> {
                        lyMsgCode.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.light_yellow
                            )
                        )
                        btnAddMsgCode.backgroundTintList = ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.dark_yellow
                        )
                    }

                    "4.G" -> {
                        lyMsgCode.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.light_green
                            )
                        )
                        btnAddMsgCode.backgroundTintList = ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.dark_green
                        )
                    }
                }
            }
        }
    }

    private fun isCodeChecked(): Boolean {
        if (binding.edtxtMsg.toString().trim()
                .isEmpty() || TextUtils.isEmpty(binding.edtxtMsg.text.toString())
        ) {
            binding.edtxtMsg.error = "This field is required"
            return false
        }
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
        backPress()
    }

    fun backPress() {
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                messages.clear()
                Util.loadFragment(
                    requireActivity(),
                    AdminPanelFragment.newInstance()
                )
                true
            } else false
        }
    }

    companion object {
        fun newInstance() = Game3SettingsFragment()
    }
}