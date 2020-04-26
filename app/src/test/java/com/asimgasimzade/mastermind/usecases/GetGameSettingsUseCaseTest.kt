package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.GameSettingsRepository
import com.asimgasimzade.mastermind.data.LocalGameSettingsDataSource
import com.asimgasimzade.mastermind.data.model.GameLevel
import com.asimgasimzade.mastermind.data.model.GameSettings
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetGameSettingsUseCaseTest {
    private lateinit var cut: GetGameSettingsUseCase

    @Mock
    lateinit var gameSettingsRepository: GameSettingsRepository

    @Before
    fun setUp() {
        cut = GetGameSettingsUseCase(gameSettingsRepository)
    }

    @Test
    fun `When execute then get game settings from local repository with no errors`() {
        // Given
        val expectedGameSettings = GameSettings(
            level = GameLevel.MEDIUM,
            areDuplicatesAllowed = false,
            areBlanksAllowed = false
        )

        given { gameSettingsRepository.get() }.willReturn(Single.just(expectedGameSettings))

        // When
        val actual = cut.execute()

        // Then
        actual.test()
            .assertResult(expectedGameSettings)
            .assertComplete()
            .assertNoErrors()
    }
}