package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.gamedata.GameDataRepository
import com.asimgasimzade.mastermind.data.gamesettings.GameSettingsRepository
import com.asimgasimzade.mastermind.data.gamesettings.GameSettingsRepositoryType
import com.asimgasimzade.mastermind.data.model.*
import com.nhaarman.mockito_kotlin.given
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveGameDataUseCaseTest {
    private lateinit var cut: SaveGameDataUseCase

    @Mock
    lateinit var gameDataRepository: GameDataRepository

    @Before
    fun setUp() {
        cut = SaveGameDataUseCase(gameDataRepository)
    }

    @Test
    fun `Given gameData when execute then save gameData to local repository with no errors`() {
        // Given
        val gameData = GameData(
            secret = MutableList(4) {
                CodePeg(CodePegColor.values()[it])
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

        // When
        val actual = cut.execute(gameData)

        // Then
        actual.test()
            .assertComplete()
            .assertNoErrors()
    }
}