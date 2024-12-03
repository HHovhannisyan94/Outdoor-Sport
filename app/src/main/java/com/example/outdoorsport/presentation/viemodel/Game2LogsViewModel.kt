package com.example.outdoorsport.presentation.viemodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorsport.data.Game2LogsFirebase
import com.example.outdoorsport.data.model.Game2Logs
import com.example.outdoorsport.usecase.Game2LogsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Game2LogsViewModel @Inject constructor(private val game2LogsUseCases: Game2LogsUseCases) : ViewModel() {
    fun readAllGame2Logs(): Flow<List<Game2Logs>> {
        return game2LogsUseCases.readGame2Logs()
    }

    fun addGame2Log(game2Logs: Game2Logs) {
        viewModelScope.launch(Dispatchers.IO) {
            game2LogsUseCases.addGame2Logs(game2Logs)
        }
    }

    fun addGame2Data(context: Context, game2LogsFirebase: Game2LogsFirebase, gameLogs:String) {
        viewModelScope.launch(Dispatchers.IO) {
            game2LogsUseCases.addGame2DataFirebase(context, game2LogsFirebase, gameLogs)
        }
    }

    fun deleteAllGame2Logs() {
        viewModelScope.launch(Dispatchers.IO) {
            game2LogsUseCases.deleteAllGame2Logs()
        }
    }
}