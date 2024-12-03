package com.example.outdoorsport.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.outdoorsport.data.model.TeamCodeMsg


@Database(entities = [TeamCodeMsg::class], version = 1, exportSchema = true)
abstract class TeamCodeMsgDatabase : RoomDatabase() {
    abstract fun teamCodeMsgDao(): TeamCodeMsgDAO
}