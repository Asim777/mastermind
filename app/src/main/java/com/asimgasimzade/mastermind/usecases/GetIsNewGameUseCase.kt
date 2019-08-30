package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.gamedata.GameDataRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetIsNewGameUseCase @Inject constructor(
    private val gameDataRepository: GameDataRepository
) {
    fun execute() = gameDataRepository.getIsNewGame()
}