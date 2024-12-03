package com.example.outdoorsport.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.outdoorsport.data.model.Game2Logs
import kotlinx.coroutines.flow.Flow


@Dao
interface Game2LogsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGame2Log(game2Logs: Game2Logs)

    @Delete
    suspend fun deleteCode(game2Logs: Game2Logs)

    @Query("DELETE FROM game2_logs_table")
    suspend fun deleteAllGame2Logs()

    @Query("SELECT * FROM game2_logs_table")
    fun readAllGame2Data(): Flow<List<Game2Logs>>
}