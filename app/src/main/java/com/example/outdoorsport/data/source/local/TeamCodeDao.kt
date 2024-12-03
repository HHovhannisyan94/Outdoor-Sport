package com.example.outdoorsport.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.outdoorsport.data.model.TeamCodes
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamCodesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) 
    suspend fun addCode(teamCodes: TeamCodes)

    @Delete
    suspend fun deleteCode(teamCodes: TeamCodes)

    @Query("DELETE FROM team_codes_table")
    suspend fun deleteAllCodes()

    @Query("SELECT * FROM team_codes_table")
    fun readAllData(): Flow<List<TeamCodes>>
}