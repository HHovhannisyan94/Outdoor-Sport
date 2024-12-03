package com.example.outdoorsport.domain.repository

import com.example.outdoorsport.data.source.local.TeamCodeMsgDAO
import com.example.outdoorsport.data.model.TeamCodeMsg
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TeamCodeMsgRepository @Inject constructor(private val teamCodeMsgDAO: TeamCodeMsgDAO) {

    fun readAllCodeMessages(): Flow<List<TeamCodeMsg>> {
        return teamCodeMsgDAO.readAllData()
    }

    suspend fun addTeamCodeMsg(teamCodeMsg: TeamCodeMsg) {
        teamCodeMsgDAO.addCodeMsg(teamCodeMsg)
    }

    suspend fun deleteTeamCodeMsg(teamCodeMsg: TeamCodeMsg) {
        teamCodeMsgDAO.deleteCodeMsg(teamCodeMsg)
    }

    suspend fun deleteAllTeamCodeMsg() {
        teamCodeMsgDAO.deleteAllCodesMsg()
    }
}