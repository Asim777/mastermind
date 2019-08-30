package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.model.CodePeg
import com.asimgasimzade.mastermind.data.model.GuessHintModel
import com.asimgasimzade.mastermind.data.model.KeyPeg
import com.asimgasimzade.mastermind.data.model.KeyPegValue
import dagger.Reusable
import javax.inject.Inject

@Reusable
class EvaluateGuessUseCase @Inject constructor(
    private val areColorsInRightPositionsUseCase: AreColorsInRightPositionsUseCase
) {
    fun execute(guessHintModel: GuessHintModel, secret: Array<CodePeg>): GuessHintModel {
        val hint = hashMapOf<CodePeg, KeyPeg>()
        val areGuessesInRightPositions = areColorsInRightPositionsUseCase.execute(
            guessHintModel,
            secret
        )
        guessHintModel.guess.forEachIndexed { index, guessColor ->
            val isColorInSecret = secret.contains(guessColor)

            if (isColorInSecret) {
                if (areGuessesInRightPositions[index]) {
                    if (hint[guessColor]?.color == KeyPegValue.WHITE) {
                        hint.remove(guessColor)
                    }
                    hint[guessColor] = KeyPeg(KeyPegValue.RED)
                } else {
                    if (!hint.containsKey(guessColor))
                        hint[guessColor] = KeyPeg(KeyPegValue.WHITE)
                }
            }
        }

        //Add empty key pegs for missing values
        val hintList = hint.values.toMutableList()
        while (hintList.size < 4) {
            hintList.add(KeyPeg(KeyPegValue.EMPTY))
        }

        return guessHintModel.copy(
            hint = hintList,
            isGuessCorrect = hintList.filter { it == KeyPeg(KeyPegValue.RED) }.size == 4
        )
    }
}