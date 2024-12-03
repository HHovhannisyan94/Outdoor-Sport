package com.example.outdoorsport.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.outdoorsport.data.model.Game1Logs

object Game1LogsDiffUtil : DiffUtil.ItemCallback<Game1Logs>() {

    override fun areItemsTheSame(oldItem: Game1Logs, newItem: Game1Logs): Boolean =
        oldItem.uid == newItem.uid

    override fun areContentsTheSame(oldItem: Game1Logs, newItem: Game1Logs): Boolean =
        oldItem == newItem
}