package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.model.CodePeg
import dagger.Reusable
import io.reactivex.Single
import javax.inject.Inject

@Reusable
class GenerateSecretUseCase @Inject constructor() {
    fun execute(): Single<List<CodePeg>> = Single.just(
        MutableList(8) {
            CodePeg(it)
        }.shuffled().take(4)
    )
}