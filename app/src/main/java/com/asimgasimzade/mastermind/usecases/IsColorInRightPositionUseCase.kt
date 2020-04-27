package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.model.CodePeg
import com.asimgasimzade.mastermind.data.model.CodePegColor
import dagger.Reusable
import io.reactivex.Single
import javax.inject.Inject

@Reusable
class IsColorInRightPositionUseCase @Inject constructor() {
    fun execute(index: Int, color: CodePegColor, secret: List<CodePeg>) =
        Single.just(secret[index].color == color)
}