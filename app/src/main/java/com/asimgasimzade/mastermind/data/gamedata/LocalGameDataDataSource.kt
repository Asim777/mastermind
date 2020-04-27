package com.asimgasimzade.mastermind.data.gamedata

import android.content.SharedPreferences
import com.asimgasimzade.mastermind.data.model.GameData
import com.asimgasimzade.mastermind.data.model.GameLevel
import com.asimgasimzade.mastermind.data.model.GameSettings
import com.google.gson.Gson
import io.reactivex.Single
import javax.inject.Inject

const val GAME_DATA_SHARED_PREFERENCES_KEY = "game data shared preferences key"

class LocalGameDataDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : GameDataDataSource {

    override fun get() = Single.just(
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

    override fun save(gameData: GameData) {
        sharedPreferences.edit()
            .putString(GAME_DATA_SHARED_PREFERENCES_KEY, gson.toJson(gameData)).apply()
    }
}