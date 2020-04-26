package com.asimgasimzade.mastermind.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class GameSettings(
    val level: GameLevel,
    val areDuplicatesAllowed: Boolean,
    val areBlanksAllowed: Boolean
)

enum class GameLevel {
    EASY,
    MEDIUM,
    HARD
}

@Parcelize
enum class GameMode: Parcelable {
    SINGLE_PLAYER,
    MULTIPLAYER;

    companion object {
        fun from(name: String) = values().firstOrNull { it.name == name }
    }
}