package com.example.testcaseminigame.data.repositories.remote

import com.example.testcaseminigame.data.api.DadJokesApi
import com.example.testcaseminigame.util.Resource
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object DadJokeImpl : DadJokeRepository {
    private val dadJokeApi: DadJokesApi

    init {
        val retrofit =
            Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create())
                .baseUrl("https://icanhazdadjoke.com")
                .build()
        dadJokeApi = retrofit.create()
    }

    override suspend fun getRandomJoke(): Resource<String> {
        return try {
            val randomJoke = dadJokeApi.getRandomDadJoke().joke
            Resource.Success(randomJoke)
        } catch (e: Exception) {
            Resource.Error("Failed request", "")
        }
    }

}