package com.example.outdoorsport.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game1_logs_table")
data class Game1Logs(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "team") val team: String?,
    @ColumnInfo(name = "duration") val duration: String?,
    @ColumnInfo(name = "resetTime") val resetTime: String?,
    @ColumnInfo(name = "detail") val detail: String?
)
