package com.asimgasimzade.mastermind.data.gamedata

import android.content.SharedPreferences
import com.asimgasimzade.mastermind.data.model.GameData
import com.google.gson.Gson
import io.reactivex.Single
import javax.inject.Inject

const val GAME_DATA_SHARED_PREFERENCES_KEY = "game data shared preferences key"
const val IS_NEW_GAME_SHARED_PREFERENCES_KEY = "is new game shared preferences key"

class LocalGameDataDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : GameDataDataSource {

    override fun getGameData() = Single.just(
        sharedPreferences.getString(
            GAME_DATA_SHARED_PREFERENCES_KEY,
            null
        )?.let { gameDataString ->
            gson.fromJson(
                gameDataString,
                GameData::class.java
            )
        }
    )

    override fun saveGameData(gameData: GameData) {
        sharedPreferences.edit()
            .putString(GAME_DATA_SHARED_PREFERENCES_KEY, gson.toJson(gameData)).apply()
    }

    override fun getIsNewGame() = Single.just(
        sharedPreferences.getBoolean(
            IS_NEW_GAME_SHARED_PREFERENCES_KEY,
            true
        )
    )

    override fun setIsNewGame(isNewGame: Boolean) {
        sharedPreferences.edit()
            .putBoolean(IS_NEW_GAME_SHARED_PREFERENCES_KEY, isNewGame).apply()
    }
}