package com.asimgasimzade.mastermind.data.model

import com.asimgasimzade.mastermind.R

data class CodePeg(
    val color: CodePegColor = CodePegColor.EMPTY
)

enum class CodePegColor(val image: Int) {
    BLUE(R.drawable.ic_blue_peg),
    GREEN(R.drawable.ic_green_peg),
    YELLOW(R.drawable.ic_yellow_peg),
    RED(R.drawable.ic_red_peg),
    WHITE(R.drawable.ic_white_peg),
    BLACK(R.drawable.ic_black_peg),
    PINK(R.drawable.ic_pink_peg),
    EMPTY(R.drawable.ic_empty_peg)
}

fun get4EmptyPegs() = Array(4) {
    CodePeg(CodePegColor.EMPTY)
}