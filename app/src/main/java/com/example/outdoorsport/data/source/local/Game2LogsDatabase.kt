package com.example.outdoorsport.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.outdoorsport.data.model.Game2Logs

@Database(entities = [Game2Logs::class], version = 1, exportSchema = true)
abstract class Game2LogsDatabase : RoomDatabase() {
    abstract fun game2LogsDao(): Game2LogsDao
}