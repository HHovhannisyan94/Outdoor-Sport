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
import com.example.outdoorsport.presentation.adapter.CodeMsgClickDeleteInterface
import com.example.outdoorsport.presentation.adapter.CodeMsgClickInterface
import com.example.outdoorsport.presentation.adapter.TeamCodesMsgRVAdapter
import com.example.outdoorsport.databinding.FragmentCodeMsgListBinding
import com.example.outdoorsport.data.model.TeamCodeMsg
import com.example.outdoorsport.utils.toast
import com.example.outdoorsport.presentation.viemodel.TeamCodesMsgViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CodeMsgListFragment : DialogFragment(), CodeMsgClickDeleteInterface, CodeMsgClickInterface {
    lateinit var binding: FragmentCodeMsgListBinding
    private val teamCodesViewModel: TeamCodesMsgViewModel by viewModels(ownerProducer = { requireActivity() })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodeMsgListBinding.inflate(inflater, container, false)
        val adapter = TeamCodesMsgRVAdapter(requireActivity(), this, this)
        binding.codesRV.adapter = adapter
        binding.codesRV.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            teamCodesViewModel.readAllCodeMessages().collect { code ->
                adapter.submitList(code)
                if (code.isNotEmpty()) {
                    binding.btnDeleteAll.visibility = View.VISIBLE
                } else {
                    binding.btnDeleteAll.visibility = View.GONE
                    binding.tvEmptyList.visibility = View.VISIBLE
                }
            }
        }

        binding.btnDeleteAll.setOnClickListener {
            teamCodesViewModel.deleteAllCodeMsg()
            with(Game3Fragment) {
                if (redTeamCodes.isNotEmpty() && redTeamMsgs.isNotEmpty()) {
                    redTeamCodes.clear()
                    redTeamMsgs.clear()
                    indexCodesRed = 0
                }
                if (blueTeamCodes.isNotEmpty() && blueTeamMsgs.isNotEmpty()) {
                    blueTeamCodes.clear()
                    blueTeamMsgs.clear()
                    indexCodesBlue = 0
                }
                if (yellowTeamCodes.isNotEmpty() && yellowTeamMsgs.isNotEmpty()) {
                    yellowTeamCodes.clear()
                    yellowTeamMsgs.clear()
                    indexCodesYellow = 0
                }
                if (greenTeamCodes.isNotEmpty() && greenTeamMsgs.isNotEmpty()) {
                    greenTeamCodes.clear()
                    greenTeamMsgs.clear()
                    indexCodesGreen = 0
                }

                if (allTeamCodes.isNotEmpty() && allTeamMsgs.isNotEmpty()) {
                    allTeamCodes.clear()
                    allTeamMsgs.clear()
                    allTeamCodeMsgIndex = 0

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

    override fun onDeleteIconClick(teamCodeMsg: TeamCodeMsg) {
        teamCodesViewModel.deleteTeamCodeMsg(teamCodeMsg)
        context?.toast { "${teamCodeMsg.team}'s ${teamCodeMsg.code} Deleted" }
        removeCodeAndMsg(teamCodeMsg)
    }


    companion object {
        fun newInstance() = CodeListFragment()
        var checkbox = false
    }

    override fun onChecked(teamCodeMsg: TeamCodeMsg) {
        val teamCode = teamCodeMsg.code.toString().replace("Code: ", "")
        val teamMsg = teamCodeMsg.message.toString().replace("Message: ", "")
        when (teamCodeMsg.team) {
            "Red" -> {
                if (!Game3Fragment.redTeamCodes.contains(teamCode)) {
                    Game3Fragment.redTeamCodes.add(teamCode)
                    Game3Fragment.redTeamMsgs.add(teamMsg)
                }
            }

            "Blue" -> {
                if (!Game3Fragment.blueTeamCodes.contains(teamCode)) {
                    Game3Fragment.blueTeamCodes.add(teamCode)
                    Game3Fragment.blueTeamMsgs.add(teamMsg)
                }
            }

            "Yellow" -> {
                if (!Game3Fragment.yellowTeamCodes.contains(teamCode)) {
                    Game3Fragment.yellowTeamCodes.add(teamCode)
                    Game3Fragment.yellowTeamMsgs.add(teamMsg)
                }
            }

            "Green" -> {
                if (!Game3Fragment.greenTeamCodes.contains(teamCode)) {
                    Game3Fragment.greenTeamCodes.add(teamCode)
                    Game3Fragment.greenTeamMsgs.add(teamMsg)
                }
            }

            "All of Teams" -> {
                if (!Game3Fragment.allTeamCodes.contains(teamCode)) {
                    Game3Fragment.allTeamCodes.add(teamCode)
                    Game3Fragment.allTeamMsgs.add(teamMsg)
                }
            }
        }
    }

    override fun onUnchecked(teamCodeMsg: TeamCodeMsg) {
        removeCodeAndMsg(teamCodeMsg)
    }

    private fun removeCodeAndMsg(teamCodeMsg: TeamCodeMsg) {
        val teamCode = teamCodeMsg.code.toString().replace("Code: ", "")
        val teamMsg = teamCodeMsg.message.toString().replace("Message: ", "")
        when (teamCodeMsg.team) {
            "Red" -> {
                Game3Fragment.redTeamCodes.remove(teamCode)
                Game3Fragment.redTeamMsgs.remove(teamMsg)
            }

            "Blue" -> {
                Game3Fragment.blueTeamCodes.remove(teamCode)
                Game3Fragment.blueTeamMsgs.remove(teamMsg)
            }

            "Yellow" -> {
                Game3Fragment.yellowTeamCodes.remove(teamCode)
                Game3Fragment.yellowTeamMsgs.remove(teamMsg)
            }

            "Green" -> {
                Game3Fragment.greenTeamCodes.remove(teamCode)
                Game3Fragment.greenTeamMsgs.remove(teamMsg)
            }

            "All of Teams" -> {
                Game3Fragment.allTeamCodes.remove(teamCode)
                Game3Fragment.allTeamMsgs.remove(teamMsg)
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
}