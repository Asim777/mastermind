package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.model.CodePeg
import com.asimgasimzade.mastermind.data.model.GuessHintModel
import com.asimgasimzade.mastermind.data.model.KeyPeg
import com.asimgasimzade.mastermind.data.model.KeyPegValue
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import dagger.Reusable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

@Reusable
class EvaluateGuessUseCase @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val isColorInRightPositionUseCase: IsColorInRightPositionUseCase
) {
    fun execute(guessHintModel: GuessHintModel, secret: List<CodePeg>): Single<GuessHintModel> {
        val hint = HashMap<CodePeg, KeyPeg>()
        guessHintModel.guess.forEachIndexed { index, guessColor ->
            val isColorInSecret = secret.contains(guessColor)

            if (isColorInSecret) {
                isColorInRightPositionUseCase.execute(index, guessColor.color, secret)
                    .compose(schedulerProvider.doOnComputationObserveOnMainSingle())
                    .subscribe({ isColorInRightPosition ->
                        if (isColorInRightPosition) {
                            if (hint[guessColor]?.color == KeyPegValue.WHITE) {
                                hint.remove(guessColor)
                            }
                            hint[guessColor]?.color = KeyPegValue.WHITE
                        } else {
                            if (!hint.containsKey(guessColor)) hint[guessColor]?.color =
                                KeyPegValue.WHITE
                        }
                    }, {
                        Timber.d(
                            "Error evaluating guess: ${guessHintModel.guess} with secret $secret"
                        )
                    })
            }
        }
        return Single.just(
            guessHintModel.copy(
                hint = hint.values.toList()
            )
        )
    }
}