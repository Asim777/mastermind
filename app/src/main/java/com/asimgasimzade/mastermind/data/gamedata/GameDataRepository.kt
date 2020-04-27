package com.asimgasimzade.mastermind.data.gamedata

import com.asimgasimzade.mastermind.data.model.GameData
import com.asimgasimzade.mastermind.data.model.GameSettings
import io.reactivex.Single
import javax.inject.Inject

interface GameDataRepositoryType {
    fun get(): Single<GameData?>
    fun save(gameData: GameData)
}

class GameDataRepository @Inject constructor(
    private val dataSource: LocalGameDataDataSource
) : GameDataRepositoryType {
    override fun get() = dataSource.get()
    override fun save(gameData: GameData) {
        dataSource.save(gameData)
    }
}