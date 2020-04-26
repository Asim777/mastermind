package com.asimgasimzade.mastermind.data.model

data class GuessHintModel(
    val guess: List<CodePeg>,
    val hint: List<KeyPeg>
)