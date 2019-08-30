package com.asimgasimzade.mastermind.data.gamedata

import com.asimgasimzade.mastermind.data.model.GameData
import dagger.Reusable
import io.reactivex.Single

@Reusable
interface GameDataDataSource {
    fun getGameData(): Single<GameData?>
    fun saveGameData(gameData: GameData)
    fun getIsNewGame(): Single<Boolean>
    fun setIsNewGame(isNewGame: Boolean)
}