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
import com.example.outdoorsport.data.DataStoreHelper
import com.example.outdoorsport.data.Game1LogsFirebase
import com.example.outdoorsport.data.model.Game1Logs
import com.example.outdoorsport.databinding.FragmentAdminClocksBinding
import com.example.outdoorsport.presentation.viemodel.Game1LogsViewModel
import com.example.outdoorsport.utils.StopWatchInstance
import com.example.outdoorsport.utils.Util
import com.example.outdoorsport.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AdminClocksFragment : Fragment() {
    lateinit var binding: FragmentAdminClocksBinding
    private val viewModel: Game1LogsViewModel by viewModels()
    private lateinit var sdf: SimpleDateFormat
    private lateinit var dataStoreHelper: DataStoreHelper

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = FragmentAdminClocksBinding.inflate(inflater, container, false)
        dataStoreHelper = DataStoreHelper(requireContext())

        sdf = SimpleDateFormat("dd/MM/yyyy  HH:mm:ss", Locale.getDefault())

        lifecycleScope.launch {
            dataStoreHelper.gameLogsFlow.collect { result ->
                clickListeners(result)
            }
        }

        return binding.root
    }

    private fun clickListeners(gameLogs: String) {
        binding.btnLogs.setOnClickListener {
            Util.loadFragment(requireActivity(), Game1LogsFragment.newInstance())
        }

        binding.btnBack.setOnClickListener {
            Util.loadFragment(requireActivity(), AdminPanelFragment.newInstance())
        }

        binding.btnBack2.setOnClickListener {
            Util.loadFragment(requireActivity(), AdminPanelFragment.newInstance())
        }
        // Use the Kotlin extension in the fragment-ktx artifact.
        binding.btnResetAll.setOnClickListener {
            resetTimer(
                "All of Teams",
                "All clocks reset!",
                StopWatchInstance.TIMER_ALL
            )
            lifecycleScope.launch {
                StopWatchInstance.reset(StopWatchInstance.TIMER_ALL, requireContext())
            }
            viewModel.deleteAllGame1Logs()

            viewModel.addGame1Data(
                requireContext(), Game1LogsFirebase("", "All the clocks reset!"), gameLogs
            )

            binding.btnReset1.isClickable = false
            binding.btnReset2.isClickable = false
            binding.btnReset3.isClickable = false
            binding.btnReset4.isClickable = false
        }

        binding.btnReset1.setOnClickListener {
            resetTimer(
                "Red",
                "The Red Clock reset!",
                StopWatchInstance.TIMER1
            )
            viewModel.addGame1Data(
                requireContext(),
                Game1LogsFirebase("", "The Red Clock reset!"), gameLogs
            )
        }

        binding.btnReset2.setOnClickListener {
            resetTimer(
                "Blue",
                "The Blue Clock reset!",
                StopWatchInstance.TIMER2
            )
            viewModel.addGame1Data(
                requireContext(),
                Game1LogsFirebase("", "The Blue Clock reset!"), gameLogs
            )
        }

        binding.btnReset3.setOnClickListener {
            resetTimer(
                "Yellow",
                "The Yellow Clock reset!",
                StopWatchInstance.TIMER3
            )
            viewModel.addGame1Data(
                requireContext(),
                Game1LogsFirebase("", "The Yellow Clock reset!"), gameLogs
            )
        }

        binding.btnReset4.setOnClickListener {
            resetTimer(
                "Green",
                "The Green Clock reset!",
                StopWatchInstance.TIMER4
            )
            viewModel.addGame1Data(
                requireContext(),
                Game1LogsFirebase("", "The Green Clock reset!"), gameLogs
            )
        }
    }

    private fun resetTimer(team: String, type: String, index: Int) {
        viewModel.addGame1Log(Game1Logs(0, team, "", sdf.format(Date()), type))
        lifecycleScope.launch {
            StopWatchInstance.reset(index, requireContext())
        }
        toast { "Success" }
    }

}