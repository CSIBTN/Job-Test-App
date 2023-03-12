package com.example.testcaseminigame.data.repositories.remote

interface FirebaseRemoteSettingsRepository {
    fun setUpRemoteSettings()
    fun fetchAndApplyRemoteConfig(update: () -> Unit, action: () -> Unit)
}