package com.example.outdoorsport.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.outdoorsport.R
import com.example.outdoorsport.databinding.TeamCodesRvItemBinding
import com.example.outdoorsport.data.model.TeamCodes
import com.example.outdoorsport.utils.toast


class TeamCodesRVAdapter(
    val context: Context,
    private val codeClickDeleteInterface: CodeClickDeleteInterface
) : ListAdapter<TeamCodes, TeamCodesRVAdapter.ViewHolder>(TeamCodesDiffUtil) {
    inner class ViewHolder(val binding: TeamCodesRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TeamCodesRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            with(currentList[position]) {
                tvTeam.text = team
                tvCode.text = code

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
                codeClickDeleteInterface.onDeleteIconClick(currentList[position])
                true
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

interface CodeClickDeleteInterface {
    fun onDeleteIconClick(teamCodes: TeamCodes)
}

interface ReuseCodesClickInterface {
    fun onClickReuse(teamCodes: TeamCodes)
}

