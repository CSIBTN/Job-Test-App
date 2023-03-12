package com.example.testcaseminigame.data

import com.example.testcaseminigame.R
import com.example.testcaseminigame.data.models.Question

object QuizQuestions {
    val questionList: List<Question>
        get() = listOf(
            Question(
                1,
                1,
                "What's the largest building in the World?",
                listOf(
                    "London Gerkin",
                    "Burj Khalifa",
                    "Empire State Building",
                    "Shanghai Tower"
                ),
                R.drawable.skyscrapers
            ),
            Question(
                2,
                3,
                "Which country this flag belongs to?",
                listOf(
                    "USA",
                    "CHINA",
                    "Spain",
                    "Peru",
                    "Italy"
                ),
                R.drawable.peru
            ),
            Question(
                3,
                1,
                "Calculate this extremely hard expression!!!",
                listOf(
                    "4"
                ),
                R.drawable.extremely_hard_expression
            ),
            Question(
                4,
                2,
                "Who's the biggest drinker in the world!?",
                listOf(
                    "Britain",
                    "Scotland",
                    "Belarus",
                    "USA"
                ),
                R.drawable.beer
            ),
            Question(
                5,
                3,
                "Now solve this equation!",
                listOf(
                    "2",
                    "3",
                    "12.5",
                    "I don't know!"
                ),
                R.drawable.hard_equation
            )
        )
}