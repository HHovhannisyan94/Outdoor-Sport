package com.example.outdoorsport.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.outdoorsport.presentation.adapter.CodeClickDeleteInterface
import com.example.outdoorsport.presentation.adapter.ReuseCodesClickInterface
import com.example.outdoorsport.presentation.adapter.TeamCodesRVAdapter
import com.example.outdoorsport.databinding.FragmentCodeListBinding
import com.example.outdoorsport.data.model.TeamCodes
import com.example.outdoorsport.utils.toast
import com.example.outdoorsport.presentation.viemodel.TeamCodesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CodeListFragment : DialogFragment(), CodeClickDeleteInterface, ReuseCodesClickInterface {
    lateinit var binding: FragmentCodeListBinding
    private val teamCodesViewModel: TeamCodesViewModel by viewModels(ownerProducer = { requireActivity() })
    lateinit var adapter: TeamCodesRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodeListBinding.inflate(inflater, container, false)
        adapter = TeamCodesRVAdapter(requireContext(), this)
        binding.codesRV.adapter = adapter
        binding.codesRV.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            teamCodesViewModel.readAllCodes().collect { code ->
                adapter.submitList(code)
                if (code.isNotEmpty()) {
                    binding.btnDeleteAll.visibility = View.VISIBLE
                } else {
                    binding.btnReuseAll.visibility = View.GONE
                    binding.btnDeleteAll.visibility = View.GONE
                    binding.tvEmptyList.visibility = View.VISIBLE
                }
            }
        }

        with(Game2Fragment) {
            binding.btnDeleteAll.setOnClickListener {
                teamCodesViewModel.deleteAllCodes()

                if (redTeamCodes.isNotEmpty()) {
                    redTeamCodes.clear()
                    countCodesRed = 0
                }
                if (blueTeamCodes.isNotEmpty()) {
                    blueTeamCodes.clear()
                    countCodesBlue = 0
                }
                if (yellowTeamCodes.isNotEmpty()) {
                    yellowTeamCodes.clear()
                    countCodesYellow = 0
                }
                if (greenTeamCodes.isNotEmpty()) {
                    greenTeamCodes.clear()
                    countCodesGreen = 0
                }
            }

            binding.btnReuseAll.setOnClickListener {
                toast { "Reuse clicked" }
                if (redTeamCodes.isNotEmpty()) {
                    redTeamCodes.clear()
                    countCodesRed = 0
                }
                if (blueTeamCodes.isNotEmpty()) {
                    blueTeamCodes.clear()
                    countCodesBlue = 0
                }
                if (yellowTeamCodes.isNotEmpty()) {
                    yellowTeamCodes.clear()
                    countCodesYellow = 0
                }
                if (greenTeamCodes.isNotEmpty()) {
                    greenTeamCodes.clear()
                    countCodesGreen = 0
                }

                for (teamCodes: TeamCodes in adapter.currentList) {
                    val teamCode = teamCodes.code.toString().replace("Code: ", "")
                    if (teamCodes.team == "Red") {
                        redTeamCodes.add(teamCode)
                    } else if (teamCodes.team.toString() == "Blue") {
                        blueTeamCodes.add(teamCode)
                    } else if (teamCodes.team.toString() == "Yellow") {
                        yellowTeamCodes.add(teamCode)
                    } else if (teamCodes.team.toString() == "Green") {
                        greenTeamCodes.add(teamCode)
                    } else if (teamCodes.team.toString() == "All of Teams") {
                        redTeamCodes.add(teamCode)
                        blueTeamCodes.add(teamCode)
                        yellowTeamCodes.add(teamCode)
                        greenTeamCodes.add(teamCode)
                        allTeamCodes.add(teamCode)
                    }
                }
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
                dismiss()
                true
            } else false
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDeleteIconClick(teamCodes: TeamCodes) {
        teamCodesViewModel.deleteTeamCode(teamCodes)
        context?.toast { "${teamCodes.team}'s ${teamCodes.code} Deleted" }
        removeCode(teamCodes)
    }





    private fun removeCode(teamCodes: TeamCodes) {
        val teamCode = teamCodes.code.toString().replace("Code: ", "")

        when (teamCodes.team) {
            "Red" -> {
                Game2Fragment.redTeamCodes.remove(teamCode)
            }

            "Blue" -> {
                Game2Fragment.blueTeamCodes.remove(teamCode)
            }

            "Yellow" -> {
                Game2Fragment.yellowTeamCodes.remove(teamCode)
            }

            "Green" -> {
                Game2Fragment.greenTeamCodes.remove(teamCode)
            }

            "All of Teams" -> {
                with(Game2Fragment) {
                    redTeamCodes.remove(teamCode)
                    blueTeamCodes.remove(teamCode)
                    yellowTeamCodes.remove(teamCode)
                    greenTeamCodes.remove(teamCode)
                    allTeamCodes.remove(teamCode)

                }
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Log.d("ABSDIALOGFRAG", "Exception", e)
        }
    }

    override fun onClickReuse(teamCodes: TeamCodes) {
        val teamCode = teamCodes.code.toString().replace("Code: ", "")

        for (i in listOf<TeamCodes>().indices) {
            when (teamCodes.team) {
                "Red" -> {
                    Game2Fragment.redTeamCodes.add(teamCode)
                }

                "Blue" -> {
                    Game2Fragment.blueTeamCodes.add(teamCode)
                }

                "Yellow" -> {
                    Game2Fragment.yellowTeamCodes.add(teamCode)
                }

                "Green" -> {
                    Game2Fragment.greenTeamCodes.add(teamCode)
                }

                "All of Teams" -> {
                    with(Game2Fragment) {
                        redTeamCodes.add(teamCode)
                        blueTeamCodes.add(teamCode)
                        yellowTeamCodes.add(teamCode)
                        greenTeamCodes.add(teamCode)
                    }
                }
            }
        }
    }
    companion object {
        fun newInstance() = CodeListFragment()
    }


}