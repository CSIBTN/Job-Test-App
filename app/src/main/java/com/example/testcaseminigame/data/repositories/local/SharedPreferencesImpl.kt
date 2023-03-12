package com.example.testcaseminigame.data.repositories.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.testcaseminigame.util.MiniGameApplication
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

object SharedPreferencesImpl : SharedPreferencesRepository {
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create {
        MiniGameApplication.applicationContext().preferencesDataStoreFile("settings")
    }

    val storedQuery: Flow<String> = dataStore.data.map {
        it[SHARED_PREFERENCES_LINK_KEY] ?: ""
    }.distinctUntilChanged()

    val storedJoke: Flow<String> = dataStore.data.map {
        it[SHARED_PREFERENCES_JOKE_KEY] ?: ""
    }

    override suspend fun setNewCurrentLink(newLink: String) {
        dataStore.edit {
            it[SHARED_PREFERENCES_LINK_KEY] = newLink
        }
    }

    override suspend fun setNewJoke(newJoke: String) {
        dataStore.edit {
            it[SHARED_PREFERENCES_JOKE_KEY] = newJoke
        }
    }


    private val SHARED_PREFERENCES_LINK_KEY = stringPreferencesKey("last_opened_link")
    private val SHARED_PREFERENCES_JOKE_KEY = stringPreferencesKey("last_requested_joke")
}