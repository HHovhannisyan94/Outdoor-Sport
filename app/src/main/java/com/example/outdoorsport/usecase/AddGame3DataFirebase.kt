package com.example.outdoorsport.usecase

import android.content.Context
import com.example.outdoorsport.data.Game3LogsFirebase
import com.example.outdoorsport.domain.repository.Game3LogsRepository
import javax.inject.Inject

class AddGame3DataFirebase @Inject constructor(private val repository: Game3LogsRepository) {
    operator fun invoke(context: Context, game3LogsFirebase: Game3LogsFirebase, gameLogs: String) =
        repository.addGame3Data(context, game3LogsFirebase, gameLogs)
}