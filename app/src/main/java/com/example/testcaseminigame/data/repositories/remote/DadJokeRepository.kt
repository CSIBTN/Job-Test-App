package com.example.testcaseminigame.data.repositories.remote

import com.example.testcaseminigame.util.Resource

interface DadJokeRepository {
    suspend fun getRandomJoke(): Resource<String>
}