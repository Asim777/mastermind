package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.GameSettingsRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetGameSettingsUseCase @Inject constructor(
    private val gameSettingsRepository: GameSettingsRepository
) {
    fun execute() = gameSettingsRepository.get()
}