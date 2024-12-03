package com.example.outdoorsport.domain.repository

import android.content.Context
import com.example.outdoorsport.data.Game3LogsFirebase
import com.example.outdoorsport.data.model.Game3Logs
import kotlinx.coroutines.flow.Flow

interface Game3LogsRepository {
    fun readAllGame3Logs(): Flow<List<Game3Logs>>
    suspend fun addGame3Log(game3Logs: Game3Logs)
    fun addGame3Data(context: Context, game3LogsFirebase: Game3LogsFirebase, gameLogs:String)
    suspend fun deleteAllGame3Logs()
}