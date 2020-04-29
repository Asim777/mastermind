package com.asimgasimzade.mastermind.data.model

import com.asimgasimzade.mastermind.R

data class KeyPeg(
    var color: KeyPegValue = KeyPegValue.EMPTY
)

enum class KeyPegValue(val image: Int) {
    RED(R.drawable.ic_red_peg),
    WHITE(R.drawable.ic_white_peg),
    EMPTY(R.drawable.ic_empty_peg)
}