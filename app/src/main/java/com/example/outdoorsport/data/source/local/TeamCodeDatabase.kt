package com.example.outdoorsport.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.outdoorsport.data.model.TeamCodes

@Database(entities = [TeamCodes::class], version = 1, exportSchema = true)
abstract class TeamCodesDatabase : RoomDatabase() {
    abstract fun teamCodesDao(): TeamCodesDao
}