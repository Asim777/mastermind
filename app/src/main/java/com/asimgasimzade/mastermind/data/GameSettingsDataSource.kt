package com.asimgasimzade.mastermind.data

import com.asimgasimzade.mastermind.data.model.GameSettings
import dagger.Reusable
import io.reactivex.Single

@Reusable
interface GameSettingsDataSource {
    fun get(): Single<GameSettings>
    fun save(gameSettings: GameSettings)
}