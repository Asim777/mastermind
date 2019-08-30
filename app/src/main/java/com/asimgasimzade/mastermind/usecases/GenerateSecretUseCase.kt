package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.model.CodePeg
import com.asimgasimzade.mastermind.data.model.CodePegColor
import dagger.Reusable
import io.reactivex.Single
import javax.inject.Inject
import kotlin.random.Random

@Reusable
class GenerateSecretUseCase @Inject constructor() {
    fun execute(areDuplicatesAllowed: Boolean) = Single.just(
        if (areDuplicatesAllowed) {
            Array(4) {
                CodePeg(
                    CodePegColor.values()[Random.nextInt(8)]
                )
            }
        } else {
            MutableList(7) {
                CodePeg(
                    CodePegColor.values()[it]
                )
            }.shuffled().take(4).toTypedArray()
        }
    )
}