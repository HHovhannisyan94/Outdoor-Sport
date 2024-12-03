package com.example.outdoorsport.presentation.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorsport.data.model.TeamCodeMsg
import com.example.outdoorsport.domain.repository.TeamCodeMsgRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamCodesMsgViewModel @Inject constructor(val repository: TeamCodeMsgRepository) :
    ViewModel() {

    fun readAllCodeMessages(): Flow<List<TeamCodeMsg>> {
        return repository.readAllCodeMessages()
    }

    fun addTeamCodeMsg(teamCodeMsg: TeamCodeMsg) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTeamCodeMsg(teamCodeMsg)
        }
    }

    fun deleteTeamCodeMsg(teamCodeMsg: TeamCodeMsg) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTeamCodeMsg(teamCodeMsg)
        }
    }

    fun deleteAllCodeMsg() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTeamCodeMsg()
        }
    }
}