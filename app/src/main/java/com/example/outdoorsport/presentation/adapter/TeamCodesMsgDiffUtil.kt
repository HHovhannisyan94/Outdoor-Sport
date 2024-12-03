package com.example.outdoorsport.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.outdoorsport.data.model.TeamCodeMsg

object TeamCodesMsgDiffUtil : DiffUtil.ItemCallback<TeamCodeMsg>() {

    override fun areItemsTheSame(oldItem: TeamCodeMsg, newItem: TeamCodeMsg): Boolean =
        oldItem.team == newItem.team

    override fun areContentsTheSame(oldItem: TeamCodeMsg, newItem: TeamCodeMsg): Boolean =
        oldItem == newItem
}