package com.example.outdoorsport.domain.repository

import android.content.Context
import com.example.outdoorsport.data.Game1LogsFirebase
import com.example.outdoorsport.data.model.Game1Logs
import kotlinx.coroutines.flow.Flow


interface Game1LogsRepository {
    fun readAllGame1Logs(): Flow<List<Game1Logs>>
    suspend fun addGame1Log(game1Logs: Game1Logs)
    fun addGame1Data(context: Context, game1LogsFirebase: Game1LogsFirebase,gameLogs:String)
    suspend fun deleteAllGame1Logs()
}