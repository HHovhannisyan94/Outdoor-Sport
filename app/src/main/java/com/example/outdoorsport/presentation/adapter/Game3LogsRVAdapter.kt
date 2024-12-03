package com.example.outdoorsport.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.outdoorsport.R
import com.example.outdoorsport.databinding.Game2LogsRvItemBinding
import com.example.outdoorsport.data.model.Game3Logs

class Game3LogsRVAdapter(val context: Context) :
    ListAdapter<Game3Logs, Game3LogsRVAdapter.ViewHolder>(Game3LogsDiffUtil) {


    inner class ViewHolder(val binding: Game2LogsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            Game2LogsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {

            if (!currentList[position].code.isNullOrEmpty() && !currentList[position].message.isNullOrEmpty()) {
                tvLog.text =
                    currentList[position].detail + "," + " CODE:" + currentList[position].code + ",   Message:" + currentList[position].message

            } else {
                holder.binding.tvLog.text =
                    currentList[position].detail
            }

            when (currentList[position].team) {
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
    }

}