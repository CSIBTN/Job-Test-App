package com.example.testcaseminigame.data.repositories.remote

import com.example.testcaseminigame.util.Util
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class FirebaseRemoteSettingsImpl : FirebaseRemoteSettingsRepository {
    private val firebaseRemoteConfig = Firebase.remoteConfig


    fun getConfigInstance() = firebaseRemoteConfig

    override fun setUpRemoteSettings() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(
            mapOf(
                "status" to false,
                "link" to Util.youtubeRewardUrl
            )
        )
    }

    override fun fetchAndApplyRemoteConfig(update: () -> Unit, action: () -> Unit) {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                update()
            }
            action()
        }
    }
}