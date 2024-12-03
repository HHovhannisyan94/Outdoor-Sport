package com.example.outdoorsport.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.outdoorsport.data.model.Game1Logs
import kotlinx.coroutines.flow.Flow

@Dao
interface Game1LogsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGame1Log(game1Logs: Game1Logs)

    @Update
    suspend fun updateLog(game1Logs: Game1Logs)

    @Delete
    suspend fun deleteCode(game1Logs: Game1Logs)

    @Query("DELETE FROM game1_logs_table")
    suspend fun deleteAllGame1Logs()

    @Query("SELECT * FROM game1_logs_table")
    fun readAllGame1Data(): Flow<List<Game1Logs>>
}