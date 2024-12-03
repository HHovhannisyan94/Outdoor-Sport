package com.example.outdoorsport.usecase

import android.content.Context
import com.example.outdoorsport.data.Game1LogsFirebase
import com.example.outdoorsport.domain.repository.Game1LogsRepository
import javax.inject.Inject

class AddGame1DataFirebase @Inject constructor(private val repository: Game1LogsRepository) {
    operator fun invoke(context: Context, game1LogsFirebase: Game1LogsFirebase, gameLogs:String) =
        repository.addGame1Data(context, game1LogsFirebase, gameLogs)
}