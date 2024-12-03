package com.example.outdoorsport.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.outdoorsport.data.model.Game3Logs

object Game3LogsDiffUtil : DiffUtil.ItemCallback<Game3Logs>() {

    override fun areItemsTheSame(oldItem: Game3Logs, newItem: Game3Logs): Boolean =
        oldItem.uid == newItem.uid

    override fun areContentsTheSame(oldItem: Game3Logs, newItem: Game3Logs): Boolean =
        oldItem == newItem
}