package com.example.outdoorsport.usecase

import com.example.outdoorsport.domain.repository.Game2LogsRepository
import javax.inject.Inject

class ReadGame2Logs  @Inject constructor(private val repository: Game2LogsRepository) {
    operator fun invoke() = repository.readAllGame2Logs()
}