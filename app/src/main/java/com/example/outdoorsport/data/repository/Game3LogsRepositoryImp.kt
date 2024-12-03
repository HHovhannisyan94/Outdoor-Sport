package com.example.outdoorsport.data.repository

import android.content.Context
import com.example.outdoorsport.data.Game3LogsFirebase
import com.example.outdoorsport.data.source.local.Game3LogsDao
import com.example.outdoorsport.domain.repository.Game3LogsRepository
import com.example.outdoorsport.data.model.Game3Logs
import com.example.outdoorsport.utils.toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class Game3LogsRepositoryImp  @Inject constructor(private val game3LogsDao: Game3LogsDao):
    Game3LogsRepository {

    override fun readAllGame3Logs(): Flow<List<Game3Logs>> {
        return game3LogsDao.readAllGame3Data()
    }

    override suspend fun addGame3Log(game3Logs: Game3Logs) {
        game3LogsDao.addGame3Log(game3Logs)
    }

    override fun addGame3Data(context: Context, game3LogsFirebase: Game3LogsFirebase, gameLogs:String) {
        val gameName = "Decoding"
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference(gameLogs).child(gameName)
        val sdf = SimpleDateFormat("dd MM yyyy 'at' HH:mm:ss ", Locale.getDefault())
        val currentDateAndTime: String = sdf.format(Date())

        databaseReference.child("Decoding    $currentDateAndTime").setValue(game3LogsFirebase)
            .addOnCompleteListener {
                // Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener { err ->
                context.toast { "Error ${err.message}" }
            }
    }

    override suspend fun deleteAllGame3Logs() {
        game3LogsDao.deleteAllGame3Logs()
    }
}