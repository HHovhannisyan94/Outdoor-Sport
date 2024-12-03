package com.example.outdoorsport.presentation.viemodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorsport.data.Game3LogsFirebase
import com.example.outdoorsport.data.model.Game3Logs
import com.example.outdoorsport.usecase.Game3LogsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Game3LogsViewModel @Inject constructor(private val gameGame3LogsUseCases: Game3LogsUseCases) :
    ViewModel() {

    fun readAllGame3Logs(): Flow<List<Game3Logs>> {
        return gameGame3LogsUseCases.readGame3Logs()
    }

    fun addGame3Log(game3Logs: Game3Logs) {
        viewModelScope.launch(Dispatchers.IO) {
            gameGame3LogsUseCases.addGame3Logs(game3Logs)
        }
    }

    fun addGame3Data(context: Context, game3LogsFirebase: Game3LogsFirebase, gameLogs: String) {
        viewModelScope.launch(Dispatchers.IO) {
            gameGame3LogsUseCases.addGame3DataFirebase(context, game3LogsFirebase, gameLogs)
        }
    }

    fun deleteAllGame3Logs() {
        viewModelScope.launch(Dispatchers.IO) {
            gameGame3LogsUseCases.deleteAllGame3Logs()
        }
    }
}