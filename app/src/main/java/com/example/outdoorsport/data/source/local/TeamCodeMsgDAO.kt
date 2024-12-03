package com.example.outdoorsport.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.outdoorsport.data.model.TeamCodeMsg
import kotlinx.coroutines.flow.Flow


@Dao
interface TeamCodeMsgDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCodeMsg(teamCodeMsg: TeamCodeMsg)

    @Delete
    suspend fun deleteCodeMsg(teamCodeMsg: TeamCodeMsg)

    @Query("DELETE FROM team_code_msg_table")
    suspend fun deleteAllCodesMsg()

    @Query("SELECT * FROM team_code_msg_table")
    fun readAllData(): Flow<List<TeamCodeMsg>>
}