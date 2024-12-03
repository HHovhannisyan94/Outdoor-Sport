package com.example.outdoorsport.usecase

import com.example.outdoorsport.domain.repository.Game2LogsRepository
import javax.inject.Inject

class DeleteAllGame2Logs @Inject constructor(private val repository: Game2LogsRepository) {
    suspend operator fun invoke() = repository.deleteAllGame2Logs()
}