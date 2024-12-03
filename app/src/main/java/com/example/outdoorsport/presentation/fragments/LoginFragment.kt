package com.example.outdoorsport.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.outdoorsport.NetworkConnection
import com.example.outdoorsport.R
import com.example.outdoorsport.data.interfaces.CallBackInterface
import com.example.outdoorsport.databinding.FragmentLoginBinding
import com.example.outdoorsport.utils.toast
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : DialogFragment() {
    lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var callBackInterface: CallBackInterface
    private lateinit var checkNetworkConnection: NetworkConnection

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        mAuth = FirebaseAuth.getInstance()
        checkNetworkConnection = NetworkConnection(requireContext())

        binding.imgClose.setOnClickListener {
            with(binding) {
                etUserName.text?.clear()
                etPassword.text?.clear()
            }
            dismiss()
        }

        binding.btnLogin.setOnClickListener {
            checkNetworkConnection.observe(viewLifecycleOwner) { isConnected: Boolean ->
                if (isConnected) {
                    loginUserAccount()
                } else {
                    loginWithoutInternet()
                }
            }
        }

        if (binding.etUserName.requestFocus()) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etUserName, InputMethodManager.SHOW_IMPLICIT)
        }

        return binding.root
    }

    fun setCallBackInterface(callBackInterface: CallBackInterface) {
        this.callBackInterface = callBackInterface
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.97).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.60).toInt()
        dialog?.window?.setLayout(width, height)
    }

    private fun loginWithoutInternet() {
        val email = "outdoor55"
        val password = "avbzp6xx"
        if (binding.etUserName.text.toString() == email && binding.etPassword.text.toString() == password) {
            dismiss()
            callBackInterface.callBackMethod()
        } else {
            activity?.toast { "Login failed!" }

            with(binding) {
                etUserName.text?.clear()
                etPassword.text?.clear()
            }
        }
    }

    private fun loginUserAccount() {
        val email = binding.etUserName.text.toString()
        val password = binding.etPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            toast { "Please enter email!" }
            return
        }

        if (TextUtils.isEmpty(password)) {
            toast { "Please enter password!" }
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //  activity?.toast { "Login successful!" }
                    dismiss()
                    callBackInterface.callBackMethod()
                } else {
                    activity?.toast { "Login failed!" }
                    with(binding) {
                        etUserName.text?.clear()
                        etPassword.text?.clear()
                    }
                }
            }
    }

    override fun getTheme() = R.style.RoundedCornersDialog

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Log.d("ABSDIALOGFRAG", "Exception", e)
        }
    }


    companion object {
        fun newInstance() = LoginFragment()
    }
}