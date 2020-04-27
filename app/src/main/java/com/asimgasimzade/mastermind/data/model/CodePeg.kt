package com.asimgasimzade.mastermind.data.model

data class CodePeg(
    val color: CodePegColor
)

enum class CodePegColor {
    BLUE,
    GREEN,
    YELLOW,
    RED,
    WHITE,
    BLACK,
    PINK
}