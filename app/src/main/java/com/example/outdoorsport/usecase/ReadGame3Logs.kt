package com.example.outdoorsport.usecase

import com.example.outdoorsport.domain.repository.Game3LogsRepository
import javax.inject.Inject

class ReadGame3Logs  @Inject constructor(private val repository: Game3LogsRepository) {
    operator fun invoke() = repository.readAllGame3Logs()
}