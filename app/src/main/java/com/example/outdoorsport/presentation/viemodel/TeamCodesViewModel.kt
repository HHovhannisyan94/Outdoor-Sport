package com.example.outdoorsport.presentation.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorsport.data.model.TeamCodes
import com.example.outdoorsport.domain.repository.TeamCodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamCodesViewModel @Inject constructor(val repository: TeamCodeRepository) : ViewModel() {
    fun readAllCodes(): Flow<List<TeamCodes>> {
        return repository.readAllCodes()
    }

    fun addTeamCode(teamCode: TeamCodes) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTeamCodes(teamCode)
        }
    }

    fun deleteTeamCode(teamCode: TeamCodes) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTeamCode(teamCode)
        }
    }

    fun deleteAllCodes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTeamCodes()
        }
    }
}