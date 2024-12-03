package com.example.outdoorsport.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.outdoorsport.data.model.TeamCodes

object TeamCodesDiffUtil  : DiffUtil.ItemCallback<TeamCodes>() {

    override fun areItemsTheSame(oldItem: TeamCodes, newItem: TeamCodes): Boolean =
        oldItem.team == newItem.team

    override fun areContentsTheSame(oldItem: TeamCodes, newItem: TeamCodes): Boolean =
        oldItem == newItem

}