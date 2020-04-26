package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.model.CodePeg
import dagger.Reusable
import io.reactivex.Single
import javax.inject.Inject
import kotlin.random.Random

@Reusable
class GenerateSecretUseCase @Inject constructor() {
    fun execute(areDuplicatesAllowed: Boolean)= Single.just(
        if (areDuplicatesAllowed) {
            (1..4).map {
                CodePeg(
                    Random.nextInt(9)
                )
            }
        } else {
            MutableList(8) {
                CodePeg(it+1)
            }.shuffled().take(4)
        }
    )
}