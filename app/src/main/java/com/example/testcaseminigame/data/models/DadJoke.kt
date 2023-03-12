package com.example.testcaseminigame.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DadJoke(
    val joke : String
)