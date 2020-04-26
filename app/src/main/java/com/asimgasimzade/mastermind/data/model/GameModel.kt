package com.asimgasimzade.mastermind.data.model

data class GameModel(
    val secret: List<CodePeg>,
    val numberOfGuesses: Int,
    val isDuplicatesAllowed: Boolean,
    val isBlanksAllowed: Boolean
)