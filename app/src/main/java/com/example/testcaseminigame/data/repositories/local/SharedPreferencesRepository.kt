package com.example.testcaseminigame.data.repositories.local

interface SharedPreferencesRepository {
    suspend fun setNewCurrentLink(newLink: String)
    suspend fun setNewJoke(newJoke: String)
}