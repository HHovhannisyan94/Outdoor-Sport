package com.example.outdoorsport.usecase

import com.example.outdoorsport.data.model.Game1Logs
import com.example.outdoorsport.domain.repository.Game1LogsRepository
import javax.inject.Inject

class AddGame1Logs @Inject constructor(private val repository: Game1LogsRepository) {
    suspend operator fun invoke(game1Logs: Game1Logs) = repository.addGame1Log(game1Logs)
}