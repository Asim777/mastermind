package com.asimgasimzade.mastermind.data.gamesettings

import com.asimgasimzade.mastermind.data.model.GameSettings
import io.reactivex.Single
import javax.inject.Inject

interface GameSettingsRepositoryType {
    fun getGameSettings(): Single<GameSettings>
    fun saveGameSettings(gameSettings: GameSettings)
}

class GameSettingsRepository @Inject constructor(
    private val dataSource: LocalGameSettingsDataSource
) : GameSettingsRepositoryType {
    override fun getGameSettings() = dataSource.getGameSettings()
    override fun saveGameSettings(gameSettings: GameSettings) =
        dataSource.saveGameSettings(gameSettings)
}