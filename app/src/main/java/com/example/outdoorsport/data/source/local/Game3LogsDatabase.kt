package com.example.outdoorsport.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.outdoorsport.data.model.Game3Logs


@Database(entities = [Game3Logs::class], version = 1, exportSchema = true)
abstract class Game3LogsDatabase : RoomDatabase() {
    abstract fun game3LogsDao(): Game3LogsDao
}