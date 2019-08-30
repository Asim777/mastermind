package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.gamedata.GameDataRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SetIsNewGameUseCase @Inject constructor(
    private val gameDataRepository: GameDataRepository
) {
    fun execute(isNewGame: Boolean) = gameDataRepository.setIsNewGame(isNewGame)
}