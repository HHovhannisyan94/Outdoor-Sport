package com.example.outdoorsport.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.outdoorsport.data.model.Game3Logs
import kotlinx.coroutines.flow.Flow

@Dao
interface Game3LogsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGame3Log(game3Logs: Game3Logs)

    @Delete
    suspend fun deleteCode(game3Logs: Game3Logs)

    @Query("DELETE FROM game3_logs_table")
    suspend fun deleteAllGame3Logs()

    @Query("SELECT * FROM game3_logs_table")
    fun readAllGame3Data(): Flow<List<Game3Logs>>
}