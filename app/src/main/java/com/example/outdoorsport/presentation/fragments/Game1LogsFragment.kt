package com.example.outdoorsport.presentation.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.outdoorsport.presentation.adapter.Game1LogsRVAdapter
import com.example.outdoorsport.databinding.FragmentGame1LogsBinding
import com.example.outdoorsport.utils.Util
import com.example.outdoorsport.presentation.viemodel.Game1LogsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Game1LogsFragment : Fragment() {
    lateinit var binding: FragmentGame1LogsBinding
    private  val viewModel: Game1LogsViewModel by viewModels()
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = FragmentGame1LogsBinding.inflate(inflater, container, false)

        val logsAdapter = Game1LogsRVAdapter(requireContext())
        binding.recyclerView.adapter = logsAdapter

        lifecycleScope.launch {
            viewModel.readAllGame1Logs()
                .collect { log ->
                    logsAdapter.submitList(log)
                    if (log.isEmpty()) {
                        binding.noLogsTV.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        binding.noLogsTV.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                }
        }

        binding.btnBack.setOnClickListener {
            Util.loadFragment(requireActivity(), AdminClocksFragment())
        }
        return binding.root
    }


    companion object {
        fun newInstance() = Game1LogsFragment()
    }
}