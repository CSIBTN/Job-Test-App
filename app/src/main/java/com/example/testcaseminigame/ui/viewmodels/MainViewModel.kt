package com.example.testcaseminigame.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcaseminigame.data.repositories.local.SharedPreferencesImpl
import com.example.testcaseminigame.data.repositories.remote.FirebaseRemoteSettingsImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val firebaseRemoteConfig = FirebaseRemoteSettingsImpl()

    private val _visible: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val visible = _visible.asStateFlow()

    private val _status: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val status = _status.asStateFlow()

    private val _link: MutableStateFlow<String> = MutableStateFlow("")
    val link = _link.asStateFlow()

    val savedLink = SharedPreferencesImpl.storedQuery


    init {
        viewModelScope.launch {
            firebaseRemoteConfig.setUpRemoteSettings()
            delay(1500)
            _visible.value = false
        }

    }

    fun fetchAndApplyRemoteConfig(action: () -> Unit) {
        firebaseRemoteConfig.fetchAndApplyRemoteConfig(action = action, update = {
            updateStatus()
            updateLink()
        })
    }

    private fun updateStatus() {
        _status.update { firebaseRemoteConfig.getConfigInstance().getBoolean("status") }
    }

    private fun updateLink() {
        _link.update { firebaseRemoteConfig.getConfigInstance().getString("link") }
    }


    fun isSavedUrlEmpty(url: String): Boolean = url.isEmpty()
}