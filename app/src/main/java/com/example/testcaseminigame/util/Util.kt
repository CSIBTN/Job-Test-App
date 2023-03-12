package com.example.testcaseminigame.util

object Util {
    const val welcomeAppDescription =
        "Welcome to my SUPER hard quiz mini-game, where all of your knowledge will be tested! You have only 1 attempt and you can't go back! Good luck!"
    const val youtubeRewardUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley"

    const val defaultDadJoke = "I'm afraid for the calendar. Its days are numbered."

    val successQuoteList = listOf("Amazing!", "Good job!", "Superb!")
    val failureQuoteList = listOf("Meh!", "Loser!", "What a failure!")

    fun getBindingErrorString(bindingName: String): String =
        "Something went wrong with the $bindingName binding!"

    enum class Status {
        FAILURE, SUCCESS
    }

}