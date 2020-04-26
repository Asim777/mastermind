package com.asimgasimzade.mastermind.data.model

data class GameModel(
    val secret: List<CodePeg>,
    var numberOfGuesses: Int,
    var areDuplicatesAllowed: Boolean,
    var areBlanksAllowed: Boolean,
    var guesses: List<GuessHintModel>
)