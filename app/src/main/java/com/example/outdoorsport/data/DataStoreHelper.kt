package com.example.outdoorsport.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreHelper(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "TIME")
        val minuteGame2 = stringPreferencesKey("START_MINUTE_Game2")
        val secondGame2 = stringPreferencesKey("SECOND_Game2")

        val minuteGame3 = stringPreferencesKey("START_MINUTE_Game3")
        val secondGame3 = stringPreferencesKey("SECOND_Game3")
        val gameLogs = stringPreferencesKey("GAMELOGS")
    }


    suspend fun storeMinuteGame2(minute: String) {
        context.dataStore.edit {
            it[minuteGame2] = minute
        }
    }


    suspend fun storeSecondGame2(second: String) {
        context.dataStore.edit {
            it[secondGame2] = second
        }
    }

    suspend fun storeMinuteGame3(minute: String) {
        context.dataStore.edit {
            it[minuteGame3] = minute
        }
    }

    suspend fun storeSecondGame3(second: String) {
        context.dataStore.edit {
            it[secondGame3] = second
        }
    }
    suspend fun storeGameLogs(logs: String) {
        context.dataStore.edit {
            it[gameLogs] = logs
        }
    }

    val minuteGame2Flow: Flow<String> = context.dataStore.data.map {
        it[minuteGame2] ?: ""
    }

    val secondGame2Flow: Flow<String> = context.dataStore.data.map {
        it[secondGame2] ?: ""
    }

    val minuteGame3Flow: Flow<String> = context.dataStore.data.map {
        it[minuteGame3] ?: ""
    }

    val secondGame3Flow: Flow<String> = context.dataStore.data.map {
        it[secondGame3] ?: ""
    }

    val gameLogsFlow: Flow<String> = context.dataStore.data.map {
        it[gameLogs] ?: ""
    }
}