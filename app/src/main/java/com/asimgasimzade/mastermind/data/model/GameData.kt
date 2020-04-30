package com.asimgasimzade.mastermind.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class GameData(
    var secret: List<CodePeg>,
    var numberOfGuesses: Int,
    var gameMode: GameMode,
    var currentLevel: Int,
    var areDuplicatesAllowed: Boolean,
    var areBlanksAllowed: Boolean,
    var guesses: MutableList<GuessHintModel>,
    var currentPlayer: Player
)

@Parcelize
enum class GameMode: Parcelable {
    SINGLE_PLAYER,
    MULTIPLAYER
}

enum class Player {
    CODE_MAKER,
    CODE_BREAKER
}