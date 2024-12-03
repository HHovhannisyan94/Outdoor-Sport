package com.example.outdoorsport.usecase

data class Game1LogsUseCases(
    val readGame1Logs: ReadGame1Logs,
    val addGame1Logs: AddGame1Logs,
    val addGame1DataFirebase: AddGame1DataFirebase,
    val deleteAllGame1Logs: DeleteAllGame1Logs
)