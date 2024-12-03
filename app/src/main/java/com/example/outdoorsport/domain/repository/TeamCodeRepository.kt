package com.example.outdoorsport.domain.repository

import com.example.outdoorsport.data.source.local.TeamCodesDao
import com.example.outdoorsport.data.model.TeamCodes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TeamCodeRepository  @Inject constructor(private val teamCodesDao: TeamCodesDao){

    fun readAllCodes(): Flow<List<TeamCodes>> {
        return teamCodesDao.readAllData()
    }
    suspend fun addTeamCodes( teamCodes: TeamCodes) {
        teamCodesDao.addCode(teamCodes)
    }

    suspend fun deleteTeamCode(teamCodes: TeamCodes) {
        teamCodesDao.deleteCode(teamCodes)
    }
    suspend fun deleteAllTeamCodes() {
        teamCodesDao.deleteAllCodes()
    }
}