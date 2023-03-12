package com.example.testcaseminigame.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcaseminigame.data.QuizQuestions
import com.example.testcaseminigame.data.repositories.remote.DadJokeImpl
import com.example.testcaseminigame.data.repositories.local.SharedPreferencesImpl
import com.example.testcaseminigame.util.Util
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {

    private val dadJokesRepository = DadJokeImpl
    private val sharedPreferences = SharedPreferencesImpl

    private val _currentQuestion: MutableStateFlow<Int> = MutableStateFlow(1)
    val currentQuestion
        get() = _currentQuestion.asStateFlow()

    private val _savedJoke: MutableStateFlow<String> = MutableStateFlow("")
    val savedJoke
        get() = _savedJoke.asStateFlow()

    init {
        viewModelScope.launch {
            sharedPreferences.storedJoke.collectLatest { storedJoke ->
                _savedJoke.update { storedJoke }
            }
        }
    }


    fun getQuestionById(questionId: Int) = QuizQuestions.questionList[questionId - 1]

    fun updateCurrentQuestion() {
        _currentQuestion.value++
    }

    fun checkIfTheRightAnswer(pickedAnswer: Int = 1, value: String = ""): Boolean {
        if (value.isNotEmpty()) {
            return getQuestionById(currentQuestion.value).possibleAnswers[0] == value
        }
        return getQuestionById(currentQuestion.value).answerId == pickedAnswer - 1
    }

    fun setDefaultCurrentQuestion(id: Int = 1) {
        _currentQuestion.value = id
    }


    suspend fun updateJoke() {
        val newJoke = dadJokesRepository.getRandomJoke()
        newJoke.data?.let { joke ->
            setNewJoke(
                joke.ifEmpty { Util.defaultDadJoke }
            )
        }
    }

    private suspend fun setNewJoke(joke: String) = sharedPreferences.setNewJoke(joke)


}