package com.example.outdoorsport.usecase

import android.content.Context
import com.example.outdoorsport.data.Game2LogsFirebase
import com.example.outdoorsport.domain.repository.Game2LogsRepository
import javax.inject.Inject

class AddGame2DataFirebase @Inject constructor(private val repository: Game2LogsRepository) {
    operator fun invoke(context: Context, game2LogsFirebase: Game2LogsFirebase, gameLogs: String) =
        repository.addGame2Data(context, game2LogsFirebase, gameLogs)
}