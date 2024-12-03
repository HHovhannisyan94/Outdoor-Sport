package com.example.outdoorsport.domain.repository

import android.content.Context
import com.example.outdoorsport.data.Game2LogsFirebase
import com.example.outdoorsport.data.model.Game2Logs
import kotlinx.coroutines.flow.Flow


interface Game2LogsRepository {
    fun readAllGame2Logs(): Flow<List<Game2Logs>>
    suspend fun addGame2Log(game2Logs: Game2Logs)
    fun addGame2Data(context: Context, game2LogsFirebase: Game2LogsFirebase, gameLogs:String)
    suspend fun deleteAllGame2Logs()
}