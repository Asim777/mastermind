package com.asimgasimzade.mastermind.data.model

data class GuessHintModel(
    val number: String,
    val guess: Array<CodePeg>,
    val hint: List<KeyPeg>,
    var isCurrentLevel: Boolean,
    var isGuessCorrect: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GuessHintModel

        if (number != other.number) return false
        if (!guess.contentEquals(other.guess)) return false
        if (hint != other.hint) return false
        if (isCurrentLevel != other.isCurrentLevel) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number.hashCode()
        result = 31 * result + guess.contentHashCode()
        result = 31 * result + hint.hashCode()
        result = 31 * result + isCurrentLevel.hashCode()
        return result
    }
}