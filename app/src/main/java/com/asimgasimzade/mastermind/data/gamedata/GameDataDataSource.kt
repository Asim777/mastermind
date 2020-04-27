package com.asimgasimzade.mastermind.data.gamedata

import com.asimgasimzade.mastermind.data.model.GameData
import dagger.Reusable
import io.reactivex.Single

@Reusable
interface GameDataDataSource {
    fun get(): Single<GameData?>
    fun save(gameData: GameData)
}