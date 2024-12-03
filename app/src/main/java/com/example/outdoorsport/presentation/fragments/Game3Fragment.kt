package com.example.outdoorsport.presentation.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.example.outdoorsport.NetworkConnection
import com.example.outdoorsport.R
import com.example.outdoorsport.data.interfaces.CallBackInterface
import com.example.outdoorsport.databinding.FragmentGame3Binding
import com.example.outdoorsport.utils.Util
import com.example.outdoorsport.utils.toast
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class Game3Fragment : Fragment(), CallBackInterface {

    lateinit var binding: FragmentGame3Binding
    private lateinit var selectedTeam: String
    private val loginFragment = LoginFragment.newInstance()
    private val enterCodesFragment = EnterCodesFragment.newInstance()
    private var isBackPressClicked = false
    private var isAdminBtnClicked = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGame3Binding.inflate(inflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        loginFragment.setCallBackInterface(this)

        with(binding) {
            mcvTimer1.setOnClickListener {
                selectedTeam = "Red"
                println("redTeamCodes.size:" + redTeamCodes.size)
                if (redTeamCodes.isNotEmpty() || allTeamCodes.isNotEmpty()) {
                    enterCodeDialog(selectedTeam)
                } else {
                    context?.toast { "No messages for you" }
                    if (indexCodesRed == redTeamCodes.size) {
                        indexCodesRed = 0
                        redTeamCodes.clear()
                        redTeamMsgs.clear()
                    } else if (allTeamCodes.isNotEmpty()) {
                        allTeamCodes.clear()
                    }
                }
            }

            mcvTimer2.setOnClickListener {
                selectedTeam = "Blue"
                if (blueTeamCodes.isNotEmpty() || allTeamCodes.isNotEmpty()) {
                    enterCodeDialog(selectedTeam)
                } else {
                    context?.toast { "No messages for you" }
                    if (indexCodesBlue == blueTeamCodes.size) {
                        indexCodesBlue = 0
                        blueTeamCodes.clear()
                        blueTeamMsgs.clear()
                    } else if (allTeamCodes.isNotEmpty()) {
                        allTeamCodes.clear()
                    }
                }
            }

            mcvTimer3.setOnClickListener {
                selectedTeam = "Yellow"
                if (yellowTeamCodes.isNotEmpty() || allTeamCodes.isNotEmpty()) {
                    enterCodeDialog(selectedTeam)
                } else {
                    context?.toast { "No messages for you" }
                    if (indexCodesYellow == yellowTeamCodes.size) {
                        indexCodesYellow = 0
                        yellowTeamCodes.clear()
                        yellowTeamMsgs.clear()
                    } else if (allTeamCodes.isNotEmpty()) {
                        allTeamCodes.clear()
                    }
                }
            }

            mcvTimer4.setOnClickListener {
                selectedTeam = "Green"
                if (greenTeamCodes.isNotEmpty() || allTeamCodes.isNotEmpty()) {
                    enterCodeDialog(selectedTeam)
                } else {
                    context?.toast {
                        "No messages for you"
                    }
                    if (indexCodesGreen == greenTeamCodes.size) {
                        greenTeamCodes.clear()
                        greenTeamMsgs.clear()
                        indexCodesGreen = 0
                    } else if (allTeamCodes.isNotEmpty()) {
                        allTeamCodes.clear()
                    }
                }
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

        return binding.root
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                requireActivity().requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                loginFragment.show(requireActivity().supportFragmentManager, "LoginFragment")
                isBackPressClicked = true
                isAdminBtnClicked = false
                true
            } else false
        }
    }

    private fun enterCodeDialog(selectedTeam: String) {
        println("enterCodeDialog")
        val args = Bundle()
        args.putString("selectedTeam", selectedTeam)
        enterCodesFragment.arguments = args
        enterCodesFragment.show(requireActivity().supportFragmentManager, "EnterCodesFragment")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.apply {
                mcvTimer1.visibility = View.INVISIBLE
                mcvTimer2.visibility = View.INVISIBLE
                mcvTimer3.visibility = View.INVISIBLE
                mcvTimer4.visibility = View.INVISIBLE
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.apply {
                mcvTimer1.visibility = View.VISIBLE
                mcvTimer2.visibility = View.VISIBLE
                mcvTimer3.visibility = View.VISIBLE
                mcvTimer4.visibility = View.VISIBLE
            }
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

            binding.txtDecodings.let { ViewCompat.setBackground(it, shapeDrawable) }
            context?.let {
                if (isConnected) {
                    binding.txtDecodings.text = buildString {
                        append("Decoding (Online)")
                    }

                    shapeDrawable.fillColor = ContextCompat.getColorStateList(it, R.color.green)
                    shapeDrawable.setStroke(5.0f, ContextCompat.getColor(it, R.color.dark_green))
                } else {
                    binding.txtDecodings.text = buildString {
                        append("Decoding (Offline)")
                    }
                    shapeDrawable.fillColor = ContextCompat.getColorStateList(it, R.color.red)
                    shapeDrawable.setStroke(5.0f, ContextCompat.getColor(it, R.color.dark_red))
                }
            }
        }
    }

    companion object {
        fun newInstance() = Game3Fragment()
        var redTeamMsgs = mutableListOf<String>()
        var blueTeamMsgs = mutableListOf<String>()
        var yellowTeamMsgs = mutableListOf<String>()
        var greenTeamMsgs = mutableListOf<String>()
        var allTeamMsgs = mutableListOf<String>()

        var redTeamCodes = mutableListOf<String>()
        var blueTeamCodes = mutableListOf<String>()
        var yellowTeamCodes = mutableListOf<String>()
        var greenTeamCodes = mutableListOf<String>()
        var allTeamCodes = mutableListOf<String>()

        var isCodeMsgForAll = false
        var allTeamCodeMsgIndex = 0
        var indexCodesRed = 0
        var indexCodesBlue = 0
        var indexCodesYellow = 0
        var indexCodesGreen = 0
    }

    override fun callBackMethod() {
        if (isBackPressClicked) {
            Util.loadFragment(requireActivity(), GameChoiceFragment.newInstance())
        }

        if (isAdminBtnClicked) {
            Util.loadFragment(requireActivity(), Game3SettingsFragment.newInstance())
        }
    }
}