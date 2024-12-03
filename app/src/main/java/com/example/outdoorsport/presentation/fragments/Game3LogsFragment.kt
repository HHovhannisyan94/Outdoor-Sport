package com.example.outdoorsport.presentation.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.outdoorsport.presentation.adapter.Game3LogsRVAdapter
import com.example.outdoorsport.databinding.FragmentGame3LogsBinding
import com.example.outdoorsport.utils.Util
import com.example.outdoorsport.presentation.viemodel.Game3LogsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Game3LogsFragment : Fragment() {
    lateinit var binding: FragmentGame3LogsBinding
    private val viewModel: Game3LogsViewModel by activityViewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = FragmentGame3LogsBinding.inflate(inflater, container, false)

        val adapter = Game3LogsRVAdapter(requireActivity())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel.readAllGame3Logs().collect { log ->
                adapter.submitList(log)
                if (log.isNotEmpty()) {
                    binding.btnDeleteAll.visibility = View.VISIBLE
                } else {
                    binding.btnDeleteAll.visibility = View.GONE
                    binding.noLogsTV.visibility = View.VISIBLE
                }
            }
        }
        binding.btnBack.setOnClickListener {
            Util.loadFragment(requireActivity(), Game3SettingsFragment.newInstance())
        }
        binding.btnDeleteAll.setOnClickListener {
            viewModel.deleteAllGame3Logs()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                Util.loadFragment(requireActivity(), Game3SettingsFragment.newInstance())
                true
            } else false
        }
    }

    companion object {
        fun newInstance() = Game3LogsFragment()
    }
}