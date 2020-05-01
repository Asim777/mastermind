package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.gamedata.GameDataRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SetIsNewGameUseCaseTest {
    private lateinit var cut: SetIsNewGameUseCase

    @Mock
    lateinit var gameDataRepository: GameDataRepository

    @Before
    fun setUp() {
        cut = SetIsNewGameUseCase(gameDataRepository)
    }

    @Test
    fun `Given isNewGame when execute then save isNewGame to local repository with no errors`() {
        // Given
        val isNewGame = true

        // When
        val actual = cut.execute(isNewGame)

        // Then
        actual.test()
            .assertComplete()
            .assertNoErrors()
    }
}