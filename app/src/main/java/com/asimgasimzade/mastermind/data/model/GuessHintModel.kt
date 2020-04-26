package com.asimgasimzade.mastermind.data.model

data class GuessHintModel(
    val number: String,
    val guess: List<CodePeg>,
    val hint: List<KeyPeg>
)