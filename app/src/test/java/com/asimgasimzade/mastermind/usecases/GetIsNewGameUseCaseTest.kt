package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.gamedata.GameDataRepository
import com.nhaarman.mockito_kotlin.given
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetIsNewGameUseCaseTest {
    private lateinit var cut: GetIsNewGameUseCase

    @Mock
    lateinit var gameDataRepository: GameDataRepository

    @Before
    fun setUp() {
        cut = GetIsNewGameUseCase(gameDataRepository)
    }

    @Test
    fun `When execute then get isNewGame from local repository with no errors`() {
        // Given
        val expected = true

        given { gameDataRepository.getIsNewGame() }.willReturn(Single.just(true))

        // When
        val actual = cut.execute()

        // Then
        actual.test()
            .assertResult(expected)
            .assertComplete()
            .assertNoErrors()
    }
}