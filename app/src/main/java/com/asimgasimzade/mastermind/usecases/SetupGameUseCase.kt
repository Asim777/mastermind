package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.model.*
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SetupGameUseCase @Inject constructor(
    private val getGameSettingsUseCase: GetGameSettingsUseCase,
    private val getNewGameUseCase: GetNewGameUseCase
){
    fun execute(gameMode: GameMode, secret: Array<CodePeg>) = getGameSettingsUseCase.execute().flatMap { settings ->
        val numberOfGuesses = when (settings.level) {
            GameLevel.EASY -> 15
            GameLevel.MEDIUM -> 10
            GameLevel.HARD -> 8
        }

        val guesses = MutableList(numberOfGuesses) {
            getEmptyGuessHintModel(it)
        }

        guesses.first().isCurrentLevel = true

        getNewGameUseCase.execute(
            secret,
            numberOfGuesses,
            gameMode,
            guesses
        ).map { gameData ->
            gameData
        }
    }

    private fun getEmptyGuessHintModel(position: Int) = GuessHintModel(
        number = (position + 1).toString(),
        guess = Array(4) { CodePeg() },
        hint = List(4) { KeyPeg() },
        isCurrentLevel = false,
        isGuessCorrect = false
    )
}