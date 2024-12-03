package com.example.outdoorsport.presentation.fragments

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
import com.example.outdoorsport.databinding.FragmentStartGameBinding
import com.example.outdoorsport.utils.Util
import com.example.outdoorsport.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel


class StartGameFragment : Fragment(), CallBackInterface {

    lateinit var binding: FragmentStartGameBinding
    private var loginFragment = LoginFragment.newInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartGameBinding.inflate(inflater, container, false)
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

        binding.btnStart.setOnClickListener {
            loginFragment.show(requireActivity().supportFragmentManager, "LoginFragment")
        }

        binding.btnInfo.setOnClickListener {

            val bottomSheet = BottomSheetDialog()
            bottomSheet.show(requireActivity().supportFragmentManager, "ModalBottomSheet")
        }
        return binding.root
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

    companion object {
        fun newInstance() = StartGameFragment()
    }

    override fun callBackMethod() {
        Util.loadFragment(requireActivity(), GameChoiceFragment.newInstance())
    }
}