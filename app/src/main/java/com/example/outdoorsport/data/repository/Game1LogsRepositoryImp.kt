package com.example.outdoorsport.data.repository

import android.content.Context
import com.example.outdoorsport.data.Game1LogsFirebase
import com.example.outdoorsport.data.source.local.Game1LogsDao
import com.example.outdoorsport.domain.repository.Game1LogsRepository
import com.example.outdoorsport.data.model.Game1Logs
import com.example.outdoorsport.utils.toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class Game1LogsRepositoryImp @Inject constructor(private val game1LogsDao: Game1LogsDao) :
    Game1LogsRepository {

    override fun readAllGame1Logs(): Flow<List<Game1Logs>> {
        return game1LogsDao.readAllGame1Data()
    }

    override suspend fun addGame1Log(game1Logs: Game1Logs) {
        game1LogsDao.addGame1Log(game1Logs)
    }

    override fun addGame1Data(context: Context, game1LogsFirebase: Game1LogsFirebase, gameLogs:String) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val gameName = "Clocks"
        val databaseReference = firebaseDatabase.getReference(gameLogs).child(gameName)

        val sdf = SimpleDateFormat("dd MM yyyy 'at' HH:mm:ss ", Locale.getDefault())
        val currentDateAndTime: String = sdf.format(Date())

        databaseReference.child("$gameName    $currentDateAndTime")
            .setValue(game1LogsFirebase)
            .addOnCompleteListener {
                //  context.toast { "Data inserted successfully" }
            }.addOnFailureListener { err ->
                context.toast { "Error ${err.message}" }
            }


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds: DataSnapshot in snapshot.children) {
                    val detail = ds.child("detail").value.toString()
                    println("detail:$detail")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override suspend fun deleteAllGame1Logs() {
        game1LogsDao.deleteAllGame1Logs()
    }
}