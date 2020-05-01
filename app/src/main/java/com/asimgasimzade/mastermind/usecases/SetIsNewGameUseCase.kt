package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.gamedata.GameDataRepository
import dagger.Reusable
import io.reactivex.Single
import javax.inject.Inject

@Reusable
class SetIsNewGameUseCase @Inject constructor(
    private val gameDataRepository: GameDataRepository
) {
    fun execute(isNewGame: Boolean) = Single.just(
        gameDataRepository.setIsNewGame(isNewGame)
    )
}