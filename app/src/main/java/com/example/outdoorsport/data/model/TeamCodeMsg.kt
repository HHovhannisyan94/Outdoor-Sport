package com.example.outdoorsport.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "team_code_msg_table")
data class TeamCodeMsg(
    @PrimaryKey(autoGenerate = true)
    val uid: Int=0,
    @ColumnInfo(name = "team") val team: String?,
    @ColumnInfo(name = "code") val code: String?,
    @ColumnInfo(name = "message") val message: String?

)
