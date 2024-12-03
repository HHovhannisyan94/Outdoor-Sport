package com.example.outdoorsport.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.outdoorsport.R
import com.example.outdoorsport.databinding.TeamCodesMsgRvItemBinding
import com.example.outdoorsport.presentation.fragments.CodeMsgListFragment.Companion.checkbox
import com.example.outdoorsport.data.model.TeamCodeMsg
import com.example.outdoorsport.utils.toast

class TeamCodesMsgRVAdapter(
    val context: Context,
    private val codeMsgClickDeleteInterface: CodeMsgClickDeleteInterface,
    private val codeMsgClickInterface: CodeMsgClickInterface
) : ListAdapter<TeamCodeMsg, TeamCodesMsgRVAdapter.ViewHolder>(TeamCodesMsgDiffUtil) {

    inner class ViewHolder(val binding: TeamCodesMsgRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TeamCodesMsgRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            with(currentList[position]) {
                tvTeam.text = team
                tvCode.text = code
                tvMsg.text = message

                when (team) {
                    "Red" -> {
                        cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.light_red
                            )
                        )
                        cardView.strokeColor = ContextCompat.getColor(
                            context,
                            R.color.red
                        )
                    }

                    "Blue" -> {
                        cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.light_blue
                            )
                        )
                        cardView.strokeColor = ContextCompat.getColor(
                            context,
                            R.color.blue
                        )
                    }

                    "Yellow" -> {
                        cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.light_yellow
                            )
                        )
                        cardView.strokeColor = ContextCompat.getColor(
                            context,
                            R.color.dark_yellow
                        )
                    }

                    "Green" -> {
                        cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.light_green
                            )
                        )
                        cardView.strokeColor = ContextCompat.getColor(
                            context,
                            R.color.green
                        )
                    }

                    "All of Teams" -> {
                        cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.dark_white
                            )
                        )
                        cardView.strokeColor = ContextCompat.getColor(
                            context,
                            R.color.black
                        )
                    }
                }
            }

            ivDelete.setOnClickListener {
                context.toast {
                    "Long press to delete"
                }
            }
            ivDelete.setOnLongClickListener {
                codeMsgClickDeleteInterface.onDeleteIconClick(currentList[position])
                true
            }

            checkBoxReuse.setOnCheckedChangeListener { _, isChecked ->
                checkbox = if (isChecked) {
                    codeMsgClickInterface.onChecked(
                        currentList[position]
                    )
                    true
                } else {
                    codeMsgClickInterface.onUnchecked(
                        currentList[position]
                    )
                    false
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

interface CodeMsgClickDeleteInterface {
    fun onDeleteIconClick(teamCodeMsg: TeamCodeMsg)
}

interface CodeMsgClickInterface {
    fun onChecked(teamCodeMsg: TeamCodeMsg)
    fun onUnchecked(teamCodeMsg: TeamCodeMsg)
}