package com.example.testcaseminigame.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcaseminigame.data.repositories.local.SharedPreferencesImpl
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WebViewModel : ViewModel() {

    private val sharedPreferences = SharedPreferencesImpl

    private val _currentLink: MutableStateFlow<String> = MutableStateFlow("")
    val currentLink: StateFlow<String>
        get() = _currentLink.asStateFlow()

    init {
        viewModelScope.launch {
            sharedPreferences.storedQuery.collectLatest { lastLink ->
                _currentLink.update { lastLink }
            }
        }
    }

    suspend fun setUpNewCurrentLink(newLink: String) = sharedPreferences.setNewCurrentLink(newLink)
}