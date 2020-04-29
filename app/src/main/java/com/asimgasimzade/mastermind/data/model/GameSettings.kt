package com.asimgasimzade.mastermind.data.model

data class GameSettings(
    val level: GameLevel,
    val areDuplicatesAllowed: Boolean,
    val areBlanksAllowed: Boolean
)

enum class GameLevel {
    EASY,
    MEDIUM,
    HARD
}