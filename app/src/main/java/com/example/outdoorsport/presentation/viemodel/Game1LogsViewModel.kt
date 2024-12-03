package com.example.outdoorsport.presentation.viemodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorsport.data.Game1LogsFirebase
import com.example.outdoorsport.data.model.Game1Logs
import com.example.outdoorsport.usecase.Game1LogsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Game1LogsViewModel @Inject constructor(private val useCase: Game1LogsUseCases) : ViewModel() {

    fun readAllGame1Logs(): Flow<List<Game1Logs>> {
        return useCase.readGame1Logs()
    }

    fun addGame1Log(game1Logs: Game1Logs) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.addGame1Logs(game1Logs)
        }
    }

    fun addGame1Data(context: Context, game1LogsFirebase: Game1LogsFirebase, gameLogs: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.addGame1DataFirebase(context, game1LogsFirebase, gameLogs)
        }
    }

    fun deleteAllGame1Logs() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.deleteAllGame1Logs()
        }
    }
}