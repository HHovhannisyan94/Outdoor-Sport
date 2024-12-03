package com.example.outdoorsport.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.outdoorsport.data.model.Game2Logs

object Game2LogsDiffUtil : DiffUtil.ItemCallback<Game2Logs>() {

    override fun areItemsTheSame(oldItem: Game2Logs, newItem: Game2Logs): Boolean =
        oldItem.uid == newItem.uid

    override fun areContentsTheSame(oldItem: Game2Logs, newItem: Game2Logs): Boolean =
        oldItem == newItem
}