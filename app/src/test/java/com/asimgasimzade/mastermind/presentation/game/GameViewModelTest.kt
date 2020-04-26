package com.asimgasimzade.mastermind.presentation.game

import android.app.Application
import com.asimgasimzade.mastermind.data.model.*
import com.asimgasimzade.mastermind.framework.TestSchedulerProvider
import com.asimgasimzade.mastermind.usecases.GenerateSecretUseCase
import com.asimgasimzade.mastermind.usecases.GetGameSettingsUseCase
import com.nhaarman.mockito_kotlin.given
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GameViewModelTest {
    private lateinit var cut: GameViewModel

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var getGameSettingsUseCase: GetGameSettingsUseCase

    @Mock
    lateinit var generateSecretUseCase: GenerateSecretUseCase

    @Before
    fun setUp() {
        cut = GameViewModel(
            application,
            TestSchedulerProvider(),
            getGameSettingsUseCase,
            generateSecretUseCase
        )
    }

    @Test
    fun ` Given gameMode and gameSettings When setupGame then setupUi with correct gameModel`() {
        // Given
        val expectedGameModel = GameModel(
            secret = MutableList(4) {
                CodePeg(it + 1)
            },
            numberOfGuesses = 10,
            areDuplicatesAllowed = false,
            areBlanksAllowed = false,
            guesses = MutableList(10) {
                GuessHintModel(
                    number = (10-it).toString(),
                    guess = listOf(),
                    hint = listOf()
                )
            }
        )

        val expectedGameSettings = GameSettings(
            level = GameLevel.MEDIUM,
            areDuplicatesAllowed = false,
            areBlanksAllowed = false
        )

        val expectedSecret = expectedGameModel.secret

        given { getGameSettingsUseCase.execute() }.willReturn(Single.just(expectedGameSettings))

        given { generateSecretUseCase.execute(expectedGameSettings.areDuplicatesAllowed) }
            .willReturn(Single.just(expectedSecret))

        val setupUiObserver = cut.setupUi().test()

        // When
        cut.onLoad(gameMode = GameMode.SINGLE_PLAYER)

        // Then
        setupUiObserver
            .assertValue(expectedGameModel)
            .assertNoErrors()
    }
}