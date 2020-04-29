package com.asimgasimzade.mastermind.presentation.game

import android.app.Application
import com.asimgasimzade.mastermind.data.model.*
import com.asimgasimzade.mastermind.framework.TestSchedulerProvider
import com.asimgasimzade.mastermind.usecases.EvaluateGuessUseCase
import com.asimgasimzade.mastermind.usecases.GenerateSecretUseCase
import com.asimgasimzade.mastermind.usecases.GetGameSettingsUseCase
import com.asimgasimzade.mastermind.usecases.SaveGameDataUseCase
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

    @Mock
    lateinit var saveGameDataUseCase: SaveGameDataUseCase

    @Mock
    lateinit var evaluateGuessUseCase: EvaluateGuessUseCase

    @Before
    fun setUp() {
        cut = GameViewModel(
            application,
            TestSchedulerProvider(),
            getGameSettingsUseCase,
            generateSecretUseCase,
            saveGameDataUseCase,
            evaluateGuessUseCase
        )
    }

    @Test
    fun ` Given gameMode and gameSettings when setupGame then setupUi with correct gameModel`() {
        // Given
        val expectedGameModel = GameData(
            secret = MutableList(4) {
                CodePeg(CodePegColor.values()[it])
            },
            numberOfGuesses = 10,
            gameMode = GameMode.SINGLE_PLAYER,
            currentLevel = 1,
            areDuplicatesAllowed = false,
            areBlanksAllowed = false,
            guesses = MutableList(10) {
                GuessHintModel(
                    number = (10-it).toString(),
                    guess = arrayOf(),
                    hint = listOf(),
                    isCurrentLevel = true,
                    isGuessCorrect = false
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