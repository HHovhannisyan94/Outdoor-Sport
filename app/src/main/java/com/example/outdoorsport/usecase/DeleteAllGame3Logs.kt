package com.example.outdoorsport.usecase

import com.example.outdoorsport.domain.repository.Game3LogsRepository
import javax.inject.Inject

class DeleteAllGame3Logs @Inject constructor(private val repository: Game3LogsRepository) {
    suspend operator fun invoke() = repository.deleteAllGame3Logs()
}