package com.example.outdoorsport.presentation.fragments

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.outdoorsport.NetworkConnection
import com.example.outdoorsport.R
import com.example.outdoorsport.data.DataStoreHelper
import com.example.outdoorsport.data.interfaces.CallBackInterface
import com.example.outdoorsport.databinding.FragmentGameChoiceBinding
import com.example.outdoorsport.utils.Util
import com.example.outdoorsport.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import kotlinx.coroutines.launch


class GameChoiceFragment : Fragment(), CallBackInterface, AdapterView.OnItemSelectedListener {

    lateinit var binding: FragmentGameChoiceBinding
    private lateinit var dataStoreHelper: DataStoreHelper
    private var loginFragment = LoginFragment.newInstance()
    lateinit var gameLogs: Array<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        binding = FragmentGameChoiceBinding.inflate(inflater, container, false)
        dataStoreHelper = DataStoreHelper(requireActivity())
        loginFragment.setCallBackInterface(this)

        val shapeAppearanceModel = ShapeAppearanceModel()
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, android.R.attr.radius.toFloat())
            .build()

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)

        ViewCompat.setBackground(binding.txtStatus, shapeDrawable)
        val checkNetworkConnection = NetworkConnection(requireActivity())
        checkNetworkConnection.observe(viewLifecycleOwner) { isConnected: Boolean ->
            if (isConnected) {
                binding.txtStatus.text = getString(R.string.status_online)

                shapeDrawable.fillColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.green)
                shapeDrawable.setStroke(
                    5.0f,
                    ContextCompat.getColor(requireContext(), R.color.dark_green)
                )
            } else {
                toast { "No Internet" }
                binding.txtStatus.text = getString(R.string.status_offline)
                shapeDrawable.fillColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.red)
                shapeDrawable.setStroke(
                    5.0f,
                    ContextCompat.getColor(requireContext(), R.color.dark_red)
                )
            }
        }

        binding.btnGame1.setOnClickListener {
            Util.loadFragment(requireActivity(), Game1Fragment.newInstance())
        }

        binding.btnGame2.setOnClickListener {
            var minute: String? = null
            var second: String? = null

            lifecycleScope.launch {
                dataStoreHelper.minuteGame2Flow.collect { min ->
                    minute = min
                }
            }

            lifecycleScope.launch {
                dataStoreHelper.secondGame2Flow.collect { sec ->
                    second = sec
                }
            }

            if (Game2Fragment.redTeamCodes.isEmpty() && Game2Fragment.blueTeamCodes.isEmpty() && Game2Fragment.yellowTeamCodes.isEmpty() && Game2Fragment.greenTeamCodes.isEmpty()) {
                context?.toast { "No active codes to start the game" }
            } else if (minute.isNullOrEmpty() || second.isNullOrEmpty()) {
                context?.toast { "You haven't selected a minute or a second." }
            } else {
                Util.loadFragment(requireActivity(), Game2Fragment.newInstance())
            }
        }

        binding.btnGame3.setOnClickListener {
            var minute: String? = null
            var second: String? = null

            lifecycleScope.launch {
                dataStoreHelper.minuteGame3Flow.collect { min ->
                    minute = min
                }
            }
            lifecycleScope.launch {
                dataStoreHelper.secondGame3Flow.collect { sec ->
                    second = sec
                }
            }

            if ((Game3Fragment.redTeamCodes.isEmpty() && Game3Fragment.blueTeamCodes.isEmpty() && Game3Fragment.yellowTeamCodes.isEmpty() && Game3Fragment.greenTeamCodes.isEmpty()) && Game3Fragment.allTeamCodes.isEmpty()) {
                context?.toast { "No active codes to start the game" }
                if (minute.isNullOrEmpty() || second.isNullOrEmpty()) {
                    context?.toast { "You haven't selected a minute or a second." }
                }
            } else {
                Util.loadFragment(requireActivity(), Game3Fragment.newInstance())
            }
        }

        binding.btnAdmin.setOnClickListener {
            Util.loadFragment(requireActivity(), AdminPanelFragment.newInstance())
        }

        binding.btnInfo.setOnClickListener {
            BottomSheetDialog().show(requireActivity().supportFragmentManager, "ModalBottomSheet")
        }

        gameLogs = resources.getStringArray(R.array.gameLogs)
        binding.spinner.onItemSelectedListener = this@GameChoiceFragment
        val ad: ArrayAdapter<CharSequence> = ArrayAdapter<CharSequence>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            gameLogs
        )
        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.spinner.adapter = ad
        lifecycleScope.launch {
            dataStoreHelper.gameLogsFlow.collect { result ->
                if (result.isNotEmpty()) {
                    val spinnerPosition: Int = ad.getPosition(result)
                    binding.spinner.setSelection(spinnerPosition)
                }
            }
        }
        return binding.root
    }

    override fun callBackMethod() {
        Util.loadFragment(requireActivity(), AdminPanelFragment.newInstance())
    }

    override fun onResume() {
        super.onResume()
        backPress()
    }

    private fun backPress() {
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                val builder = MaterialAlertDialogBuilder(requireActivity())
                with(builder) {
                    setTitle("Do you want to exit?")
                    setCancelable(false)
                    setPositiveButton("Yes") { _, _ ->
                        requireActivity().finish()
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.cancel()
                    }
                }
                builder.create().show()

                true
            } else false
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        lifecycleScope.launch {
            dataStoreHelper.storeGameLogs(gameLogs[position])
        }
        (view as TextView).setTextColor(Color.RED) //Change selected text color
        view.setTypeface(null, Typeface.BOLD)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    companion object {
        fun newInstance() = GameChoiceFragment()
    }
}