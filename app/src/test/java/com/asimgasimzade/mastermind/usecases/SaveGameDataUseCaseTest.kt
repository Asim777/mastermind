package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.gamedata.GameDataRepository
import com.asimgasimzade.mastermind.data.model.CodePeg
import com.asimgasimzade.mastermind.data.model.CodePegColor
import com.asimgasimzade.mastermind.data.model.GameData
import com.asimgasimzade.mastermind.data.model.GuessHintModel
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

        // When
        val actual = cut.execute(gameData)

        // Then
        actual.test()
            .assertComplete()
            .assertNoErrors()
    }
}