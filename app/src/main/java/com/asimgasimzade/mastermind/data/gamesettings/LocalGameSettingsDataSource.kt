package com.asimgasimzade.mastermind.data.gamesettings

import android.content.SharedPreferences
import com.asimgasimzade.mastermind.data.model.GameLevel
import com.asimgasimzade.mastermind.data.model.GameSettings
import com.google.gson.Gson
import io.reactivex.Single
import javax.inject.Inject

const val GAME_SETTINGS_SHARED_PREFERENCES_KEY = "game settings shared preferences key"

class LocalGameSettingsDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : GameSettingsDataSource {

    override fun getGameSettings() = Single.just(
        gson.fromJson(
            sharedPreferences.getString(
                GAME_SETTINGS_SHARED_PREFERENCES_KEY,
                getDefaultSettings()
            ), GameSettings::class.java
        )
    )

    override fun saveGameSettings(gameSettings: GameSettings) {
        sharedPreferences.edit()
            .putString(GAME_SETTINGS_SHARED_PREFERENCES_KEY, gson.toJson(gameSettings)).apply()
    }

    private fun getDefaultSettings() = gson.toJson(
        GameSettings(
            level = GameLevel.MEDIUM,
            areDuplicatesAllowed = false,
            areBlanksAllowed = false
        )
    )
}