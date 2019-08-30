package com.asimgasimzade.mastermind.data.gamedata

import com.asimgasimzade.mastermind.data.model.GameData
import io.reactivex.Single
import javax.inject.Inject

interface GameDataRepositoryType {
    fun getGameData(): Single<GameData?>
    fun saveGameData(gameData: GameData)
    fun getIsNewGame(): Single<Boolean>
    fun setIsNewGame(isNewGame: Boolean)
}

class GameDataRepository @Inject constructor(
    private val dataSource: LocalGameDataDataSource
) : GameDataRepositoryType {
    override fun getGameData() = dataSource.getGameData()
    override fun saveGameData(gameData: GameData) {
        dataSource.saveGameData(gameData)
    }
    override fun getIsNewGame() = dataSource.getIsNewGame()
    override fun setIsNewGame(isNewGame: Boolean) {
        dataSource.setIsNewGame(isNewGame)
    }

}