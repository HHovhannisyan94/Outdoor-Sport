package com.example.outdoorsport.usecase

import com.example.outdoorsport.domain.repository.Game1LogsRepository
import javax.inject.Inject

class DeleteAllGame1Logs @Inject constructor(private val repository: Game1LogsRepository) {
    suspend operator fun invoke() = repository.deleteAllGame1Logs()
}