package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.model.*
import com.nhaarman.mockito_kotlin.given
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.MethodRule
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

@RunWith(Parameterized::class)
class EvaluateGuessUseCaseTest(
    private val guessHint: GuessHintModel,
    private val secret: List<CodePeg>,
    private val expected: GuessHintModel
) {
    @Mock
    lateinit var areColorInRightPositionUseCase: AreColorsInRightPositionsUseCase

    @get:Rule
    var rule: MethodRule = MockitoJUnit.rule()

    private lateinit var cut: EvaluateGuessUseCase

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Given {0} & {1} expect {2}")
        fun data(): Collection<Array<out Any?>> {
            return getCorrectGuessAndSecret() +
                    getCombinationOfWrongGuessAndSecret()
        }

        private fun getCorrectGuessAndSecret() = listOf(
            arrayOf(
                GuessHintModel(
                    number = "1",
                    guess = arrayOf(
                        CodePeg(CodePegColor.WHITE),
                        CodePeg(CodePegColor.BLACK),
                        CodePeg(CodePegColor.RED),
                        CodePeg(CodePegColor.GREEN)
                    ),
                    hint = listOf(
                        KeyPeg(KeyPegValue.EMPTY),
                        KeyPeg(KeyPegValue.EMPTY),
                        KeyPeg(KeyPegValue.EMPTY),
                        KeyPeg(KeyPegValue.EMPTY)
                    ),
                    isCurrentLevel = true,
                    isGuessCorrect = true
                ),
                getCorrectSecret(),
                GuessHintModel(
                    number = "1",
                    guess = arrayOf(
                        CodePeg(CodePegColor.WHITE),
                        CodePeg(CodePegColor.BLACK),
                        CodePeg(CodePegColor.RED),
                        CodePeg(CodePegColor.GREEN)
                    ),
                    hint = listOf(
                        KeyPeg(KeyPegValue.RED),
                        KeyPeg(KeyPegValue.RED),
                        KeyPeg(KeyPegValue.RED),
                        KeyPeg(KeyPegValue.RED)
                    ),
                    isCurrentLevel = true,
                    isGuessCorrect = true
                )

            )
        )

        private fun getCorrectSecret(): List<CodePeg> {
            return listOf(
                CodePeg(CodePegColor.WHITE),
                CodePeg(CodePegColor.BLACK),
                CodePeg(CodePegColor.RED),
                CodePeg(CodePegColor.GREEN)
            )
        }

        private fun getCombinationOfWrongGuessAndSecret() = listOf(
            arrayOf(
                GuessHintModel(
                    number = "1",
                    guess = arrayOf(
                        CodePeg(CodePegColor.WHITE),
                        CodePeg(CodePegColor.BLACK),
                        CodePeg(CodePegColor.RED),
                        CodePeg(CodePegColor.GREEN)
                    ),
                    hint = listOf(
                        KeyPeg(KeyPegValue.EMPTY),
                        KeyPeg(KeyPegValue.EMPTY),
                        KeyPeg(KeyPegValue.EMPTY),
                        KeyPeg(KeyPegValue.EMPTY)
                    ),
                    isCurrentLevel = true,
                    isGuessCorrect = false
                ),
                getIncorrectSecret(),
                GuessHintModel(
                    number = "1",
                    guess = arrayOf(
                        CodePeg(CodePegColor.WHITE),
                        CodePeg(CodePegColor.BLACK),
                        CodePeg(CodePegColor.RED),
                        CodePeg(CodePegColor.GREEN)
                    ),
                    hint = listOf(
                        KeyPeg(KeyPegValue.WHITE),
                        KeyPeg(KeyPegValue.WHITE),
                        KeyPeg(KeyPegValue.WHITE),
                        KeyPeg(KeyPegValue.EMPTY)
                    ),
                    isCurrentLevel = true,
                    isGuessCorrect = false
                )
            ),
            arrayOf(
                GuessHintModel(
                    number = "1",
                    guess = arrayOf(
                        CodePeg(CodePegColor.WHITE),
                        CodePeg(CodePegColor.GREEN),
                        CodePeg(CodePegColor.PINK),
                        CodePeg(CodePegColor.YELLOW)
                    ),
                    hint = listOf(
                        KeyPeg(KeyPegValue.EMPTY),
                        KeyPeg(KeyPegValue.EMPTY),
                        KeyPeg(KeyPegValue.EMPTY),
                        KeyPeg(KeyPegValue.EMPTY)
                    ),
                    isCurrentLevel = true,
                    isGuessCorrect = false
                ),
                getIncorrectSecret(),
                GuessHintModel(
                    number = "1",
                    guess = arrayOf(
                        CodePeg(CodePegColor.WHITE),
                        CodePeg(CodePegColor.GREEN),
                        CodePeg(CodePegColor.PINK),
                        CodePeg(CodePegColor.YELLOW)
                    ),
                    hint = listOf(
                        KeyPeg(KeyPegValue.RED),
                        KeyPeg(KeyPegValue.WHITE),
                        KeyPeg(KeyPegValue.EMPTY),
                        KeyPeg(KeyPegValue.EMPTY)
                    ),
                    isCurrentLevel = true,
                    isGuessCorrect = false
                )
            )
        )

        private fun getIncorrectSecret(): List<CodePeg> {
            return listOf(
                CodePeg(CodePegColor.BLUE),
                CodePeg(CodePegColor.GREEN),
                CodePeg(CodePegColor.WHITE),
                CodePeg(CodePegColor.RED)
            )
        }
    }

    @Before
    fun setUp() {
        cut = EvaluateGuessUseCase(areColorInRightPositionUseCase)
    }

    @Test
    fun `Given guess and secret when execute then return hint`() {
        // Given
        given { areColorInRightPositionUseCase.execute(guessHint, secret) }.willReturn(
            List(4) {
                secret[it].color == guessHint.guess[it].color
            }
        )

        // When
        val actual = cut.execute(guessHint, secret)

        // Then
        Assert.assertEquals(expected, actual)
    }
}