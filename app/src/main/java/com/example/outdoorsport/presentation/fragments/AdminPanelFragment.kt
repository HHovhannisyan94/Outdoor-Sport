package com.example.outdoorsport.presentation.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.outdoorsport.data.DataStoreHelper
import com.example.outdoorsport.databinding.FragmentAdminPanelBinding
import com.example.outdoorsport.utils.Util
import kotlinx.coroutines.launch


class AdminPanelFragment : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var binding: FragmentAdminPanelBinding
    lateinit var gameLogs: Array<String>
    private lateinit var dataStoreHelper: DataStoreHelper
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = FragmentAdminPanelBinding.inflate(inflater, container, false)
        dataStoreHelper = DataStoreHelper(requireContext())

        with(binding) {
            btnGame1.setOnClickListener {
                Util.loadFragment(requireActivity(), AdminClocksFragment())
            }
            btnGame2.setOnClickListener {
                Util.loadFragment(requireActivity(), Game2SettingsFragment.newInstance())
            }
            btnGame3.setOnClickListener {
                Util.loadFragment(requireActivity(), Game3SettingsFragment.newInstance())
            }

            btnStartGame.setOnClickListener {
                Util.loadFragment(requireActivity(), GameChoiceFragment.newInstance())
            }

        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                Util.loadFragment(requireActivity(), GameChoiceFragment.newInstance())
                true
            } else false
        }
    }


    companion object {
        fun newInstance() = AdminPanelFragment()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        lifecycleScope.launch {
            dataStoreHelper.storeGameLogs(gameLogs[position])
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}