package com.example.testcaseminigame.data.api

import com.example.testcaseminigame.data.models.DadJoke
import retrofit2.http.GET
import retrofit2.http.Headers


interface DadJokesApi {
    @Headers("Accept: application/json")
    @GET("/")
    suspend fun getRandomDadJoke(): DadJoke

}