package com.example.outdoorsport.usecase

import com.example.outdoorsport.data.model.Game2Logs
import com.example.outdoorsport.domain.repository.Game2LogsRepository
import javax.inject.Inject

class AddGame2Logs @Inject constructor(private val repository: Game2LogsRepository) {
    suspend operator fun invoke(game2Logs: Game2Logs) = repository.addGame2Log(game2Logs)
}