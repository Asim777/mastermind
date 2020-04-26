package com.asimgasimzade.mastermind.data

data class GameModel(
    val secret: List<CodePeg>,
    val numberOfGuesses: Int,
    val isDuplicatesAllowed: Boolean,
    val isBlanksAllowed: Boolean
)