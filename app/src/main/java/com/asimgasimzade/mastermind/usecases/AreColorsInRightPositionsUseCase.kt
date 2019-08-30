package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.model.CodePeg
import com.asimgasimzade.mastermind.data.model.GuessHintModel
import dagger.Reusable
import javax.inject.Inject

@Reusable
class AreColorsInRightPositionsUseCase @Inject constructor() {
    fun execute(guessHint: GuessHintModel, secret: Array<CodePeg>) =
        Array(4) {
            secret[it].color == guessHint.guess[it].color
        }
}