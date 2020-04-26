package com.asimgasimzade.mastermind.data

import com.asimgasimzade.mastermind.data.model.GameSettings
import io.reactivex.Single
import javax.inject.Inject

interface GameSettingsRepositoryType {
    fun get(): Single<GameSettings>
}

class GameSettingsRepository @Inject constructor(
    private val dataSource: LocalGameSettingsDataSource
) : GameSettingsRepositoryType {
    override fun get() = dataSource.get()
}