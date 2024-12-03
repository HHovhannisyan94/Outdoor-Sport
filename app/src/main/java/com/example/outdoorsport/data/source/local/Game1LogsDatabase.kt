package com.example.outdoorsport.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.outdoorsport.data.model.Game1Logs

@Database(entities = [Game1Logs::class], version = 1, exportSchema = true)
abstract class Game1LogsDatabase : RoomDatabase() {
    abstract fun game1LogsDao(): Game1LogsDao
}