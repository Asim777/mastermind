package com.asimgasimzade.mastermind.data

data class GuessHintModel(
    val guess: List<CodePeg>,
    val hint: List<KeyPeg>
)