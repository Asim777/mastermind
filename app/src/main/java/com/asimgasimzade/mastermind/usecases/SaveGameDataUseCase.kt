package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.gamedata.GameDataRepository
import com.asimgasimzade.mastermind.data.model.GameData
import dagger.Reusable
import io.reactivex.Single
import javax.inject.Inject

@Reusable
class SaveGameDataUseCase @Inject constructor(
    private val gameDataRepository: GameDataRepository
) {
    fun execute(gameData: GameData) = Single.just(
        gameDataRepository.saveGameData(gameData)
    )
}
