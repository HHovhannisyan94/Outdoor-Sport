package com.example.outdoorsport.data.repository

import android.content.Context
import com.example.outdoorsport.data.Game2LogsFirebase
import com.example.outdoorsport.data.source.local.Game2LogsDao
import com.example.outdoorsport.domain.repository.Game2LogsRepository
import com.example.outdoorsport.data.model.Game2Logs
import com.example.outdoorsport.utils.toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class Game2LogsRepositoryImp @Inject constructor(private val game2LogsDao: Game2LogsDao) :
    Game2LogsRepository {
    override fun readAllGame2Logs(): Flow<List<Game2Logs>> {
        return game2LogsDao.readAllGame2Data()
    }

    override suspend fun addGame2Log(game2Logs: Game2Logs) {
        game2LogsDao.addGame2Log(game2Logs)
    }

    override fun addGame2Data(context: Context, game2LogsFirebase: Game2LogsFirebase, gameLogs:String) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val gameName = "Code Generation"
        val databaseReference = firebaseDatabase.getReference(gameLogs).child(gameName)
        val sdf = SimpleDateFormat("dd MM yyyy 'at' HH:mm:ss ", Locale.getDefault())

        databaseReference.child("Code Generation  " + sdf.format(Date()))
            .setValue(game2LogsFirebase)
            .addOnCompleteListener {
                //  context.toast { "Data inserted successfully" }
            }.addOnFailureListener { err ->
                context.toast { "Error ${err.message}" }
            }
    }

    override suspend fun deleteAllGame2Logs() {
        game2LogsDao.deleteAllGame2Logs()
    }
}