package com.asimgasimzade.mastermind.data.model

data class KeyPeg(
    var color: KeyPegValue
)

enum class KeyPegValue {
    RED,
    WHITE
}