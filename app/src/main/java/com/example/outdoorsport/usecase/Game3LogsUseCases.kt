package com.example.outdoorsport.usecase

data class Game3LogsUseCases(
    val readGame3Logs: ReadGame3Logs,
    val addGame3Logs: AddGame3Logs,
    val addGame3DataFirebase: AddGame3DataFirebase,
    val deleteAllGame3Logs: DeleteAllGame3Logs
)