package com.asimgasimzade.mastermind.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class GameData(
    var secret: Array<CodePeg>,
    var numberOfGuesses: Int,
    var gameMode: GameMode,
    var currentLevel: Int,
    var areDuplicatesAllowed: Boolean,
    var areBlanksAllowed: Boolean,
    var guesses: MutableList<GuessHintModel>,
    var currentPlayer: Player
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameData

        if (!secret.contentEquals(other.secret)) return false
        if (numberOfGuesses != other.numberOfGuesses) return false
        if (gameMode != other.gameMode) return false
        if (currentLevel != other.currentLevel) return false
        if (areDuplicatesAllowed != other.areDuplicatesAllowed) return false
        if (areBlanksAllowed != other.areBlanksAllowed) return false
        if (guesses != other.guesses) return false
        if (currentPlayer != other.currentPlayer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = secret.contentHashCode()
        result = 31 * result + numberOfGuesses
        result = 31 * result + gameMode.hashCode()
        result = 31 * result + currentLevel
        result = 31 * result + areDuplicatesAllowed.hashCode()
        result = 31 * result + areBlanksAllowed.hashCode()
        result = 31 * result + guesses.hashCode()
        result = 31 * result + currentPlayer.hashCode()
        return result
    }
}

@Parcelize
enum class GameMode: Parcelable {
    SINGLE_PLAYER,
    MULTIPLAYER
}

enum class Player {
    CODE_MAKER,
    CODE_BREAKER
}