package com.asimgasimzade.mastermind.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class GameData(
    val secret: List<CodePeg>,
    var numberOfGuesses: Int,
    var gameMode: GameMode,
    var currentLevel: Int,
    var areDuplicatesAllowed: Boolean,
    var areBlanksAllowed: Boolean,
    var guesses: MutableList<GuessHintModel>
)

@Parcelize
enum class GameMode: Parcelable {
    SINGLE_PLAYER,
    MULTIPLAYER
}