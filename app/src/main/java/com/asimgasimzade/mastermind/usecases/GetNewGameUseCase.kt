package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.model.*
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetNewGameUseCase @Inject constructor(
    private val getGameSettingsUseCase: GetGameSettingsUseCase
) {
    fun execute(
        secret: Array<CodePeg>,
        numberOfGuesses: Int,
        gameMode: GameMode,
        guesses: MutableList<GuessHintModel>
    ) = getGameSettingsUseCase.execute().map { settings ->
        GameData(
            secret = secret,
            numberOfGuesses = numberOfGuesses,
            gameMode = gameMode,
            currentLevel = 1,
            areBlanksAllowed = settings.areBlanksAllowed,
            areDuplicatesAllowed = settings.areDuplicatesAllowed,
            guesses = guesses,
            currentPlayer = if (gameMode == GameMode.SINGLE_PLAYER) {
                Player.CODE_BREAKER
            } else Player.CODE_MAKER
        )
    }
}
