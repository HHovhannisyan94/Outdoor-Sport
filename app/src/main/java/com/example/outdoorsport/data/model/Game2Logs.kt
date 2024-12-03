package com.example.outdoorsport.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "game2_logs_table")
data class Game2Logs(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "team") val team: String?,
    @ColumnInfo(name = "startTime") val startTime: String?,
    @ColumnInfo(name = "detail") val detail: String?,
    @ColumnInfo(name = "code") val code: String?
)