package com.example.outdoorsport.usecase

import com.example.outdoorsport.data.model.Game3Logs
import com.example.outdoorsport.domain.repository.Game3LogsRepository
import javax.inject.Inject

class AddGame3Logs @Inject constructor(private val repository: Game3LogsRepository) {
    suspend operator fun invoke(game3Logs: Game3Logs) = repository.addGame3Log(game3Logs)
}