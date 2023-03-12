package com.example.testcaseminigame.data.models

data class Question(
    val questionId: Int,
    val answerId: Int,
    val questionText: String,
    val possibleAnswers: List<String>,
    val imageDrawable: Int ,
)