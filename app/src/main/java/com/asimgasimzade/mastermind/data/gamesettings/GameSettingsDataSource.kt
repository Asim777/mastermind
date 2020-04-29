package com.asimgasimzade.mastermind.data.gamesettings

import com.asimgasimzade.mastermind.data.model.GameSettings
import dagger.Reusable
import io.reactivex.Single

@Reusable
interface GameSettingsDataSource {
    fun getGameSettings(): Single<GameSettings>
    fun saveGameSettings(gameSettings: GameSettings)
}