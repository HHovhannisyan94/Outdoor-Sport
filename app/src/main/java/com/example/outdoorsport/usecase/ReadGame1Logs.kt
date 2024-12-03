package com.example.outdoorsport.usecase

import com.example.outdoorsport.domain.repository.Game1LogsRepository
import javax.inject.Inject

class ReadGame1Logs @Inject constructor(private val repository: Game1LogsRepository) {
     operator fun invoke() = repository.readAllGame1Logs()
}