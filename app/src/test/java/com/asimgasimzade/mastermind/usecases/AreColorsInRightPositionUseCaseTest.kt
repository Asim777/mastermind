package com.asimgasimzade.mastermind.usecases

import com.asimgasimzade.mastermind.data.model.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.MethodRule
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.junit.MockitoJUnit

@RunWith(Parameterized::class)
class AreColorsInRightPositionUseCaseTest(
    private val guessHint: GuessHintModel,
    private val secret: Array<CodePeg>,
    private val expected: Array<Boolean>

) {
    @get:Rule
    var rule: MethodRule = MockitoJUnit.rule()

    private lateinit var cut: AreColorsInRightPositionsUseCase

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Given {0} & {1} expect {2}")
        fun data(): Collection<Array<out Any?>> {
            return getCombinationWithNoDuplicatesAllowed() +
                    getCombinationWithDuplicatesAllowed() +
                    getCombinationWithNoColorsInRightPosition() +
                    getCombinationWithBlanksAllowed()

        }

        private fun getCombinationWithNoDuplicatesAllowed() = listOf(
            arrayOf(
                getGuessHintModel(getGuessWithOneRightColor()),
                getSecretWithNoDuplicates(),
                arrayOf(true, false, false, false)
            ),
            arrayOf(
                getGuessHintModel(getGuessWithTwoRightColors()),
                getSecretWithNoDuplicates(),
                arrayOf(true, true, false, false)
            ),
            arrayOf(
                getGuessHintModel(getGuessWithThreeRightColors()),
                getSecretWithNoDuplicates(),
                arrayOf(true, true, true, false)
            ),
            arrayOf(
                getGuessHintModel(getGuessWithFourRightColors()),
                getSecretWithNoDuplicates(),
                arrayOf(true, true, true, true)
            )
        )

        private fun getCombinationWithDuplicatesAllowed() = listOf(
            arrayOf(
                getGuessHintModel(getGuessWithOneRightColor()),
                getSecretWithDuplicates(),
                arrayOf(true, false, false, false)
            ),
            arrayOf(
                getGuessHintModel(getGuessWithTwoRightColors()),
                getSecretWithDuplicates(),
                arrayOf(true, true, false, false)
            ),
            arrayOf(
                getGuessHintModel(getGuessWithThreeRightColors()),
                getSecretWithDuplicates(),
                arrayOf(true, true, true, false)
            ),
            arrayOf(
                getGuessHintModel(getGuessWithFourRightColorsDuplicate()),
                getSecretWithDuplicates(),
                arrayOf(true, true, true, true)
            )
        )

        private fun getCombinationWithNoColorsInRightPosition() = listOf(
            arrayOf(
                getGuessHintModel(getGuessWithOneRightColor()),
                getSecretWithNoMatches(),
                arrayOf(false, false, false, false)
            ),
            arrayOf(
                getGuessHintModel(getGuessWithTwoRightColors()),
                getSecretWithNoMatches(),
                arrayOf(false, false, false, false)
            ),
            arrayOf(
                getGuessHintModel(getGuessWithThreeRightColors()),
                getSecretWithNoMatches(),
                arrayOf(false, false, false, false)
            ),
            arrayOf(
                getGuessHintModel(getGuessWithFourRightColors()),
                getSecretWithNoMatches(),
                arrayOf(false, false, false, false)
            )
        )

        private fun getCombinationWithBlanksAllowed() = listOf(
            arrayOf(
                getGuessHintModel(getGuessWithOneRightColor()),
                getSecretWithBlanks(),
                arrayOf(false, false, true, false)
            ),
            arrayOf(
                getGuessHintModel(getGuessWithTwoRightColors()),
                getSecretWithBlanks(),
                arrayOf(false, false, true, false)
            ),
            arrayOf(
                getGuessHintModel(getGuessWithThreeRightColors()),
                getSecretWithBlanks(),
                arrayOf(false, false, false, false)
            ),
            arrayOf(
                getGuessHintModel(getGuessWithFourRightColors()),
                getSecretWithBlanks(),
                arrayOf(false, false, false, false)
            )
        )

        private fun getSecretWithNoDuplicates() = arrayOf(
            CodePeg(CodePegColor.WHITE),
            CodePeg(CodePegColor.BLACK),
            CodePeg(CodePegColor.RED),
            CodePeg(CodePegColor.GREEN)
        )

        private fun getSecretWithDuplicates() = arrayOf(
            CodePeg(CodePegColor.WHITE),
            CodePeg(CodePegColor.BLACK),
            CodePeg(CodePegColor.RED),
            CodePeg(CodePegColor.WHITE)
        )

        private fun getSecretWithNoMatches() = arrayOf(
            CodePeg(CodePegColor.YELLOW),
            CodePeg(CodePegColor.BLUE),
            CodePeg(CodePegColor.PINK),
            CodePeg(CodePegColor.BLUE)
        )

        private fun getSecretWithBlanks() = arrayOf(
            CodePeg(CodePegColor.YELLOW),
            CodePeg(CodePegColor.EMPTY),
            CodePeg(CodePegColor.BLACK),
            CodePeg(CodePegColor.EMPTY)
        )

        private fun getGuessWithOneRightColor() = arrayOf(
            CodePeg(CodePegColor.WHITE),
            CodePeg(CodePegColor.GREEN),
            CodePeg(CodePegColor.BLACK),
            CodePeg(CodePegColor.RED)
        )

        private fun getGuessWithTwoRightColors() = arrayOf(
            CodePeg(CodePegColor.WHITE),
            CodePeg(CodePegColor.BLACK),
            CodePeg(CodePegColor.BLACK),
            CodePeg(CodePegColor.RED)
        )

        private fun getGuessWithThreeRightColors() = arrayOf(
            CodePeg(CodePegColor.WHITE),
            CodePeg(CodePegColor.BLACK),
            CodePeg(CodePegColor.RED),
            CodePeg(CodePegColor.RED)
        )

        private fun getGuessWithFourRightColors() = arrayOf(
            CodePeg(CodePegColor.WHITE),
            CodePeg(CodePegColor.BLACK),
            CodePeg(CodePegColor.RED),
            CodePeg(CodePegColor.GREEN)
        )

        private fun getGuessWithFourRightColorsDuplicate() = arrayOf(
            CodePeg(CodePegColor.WHITE),
            CodePeg(CodePegColor.BLACK),
            CodePeg(CodePegColor.RED),
            CodePeg(CodePegColor.WHITE)
        )

        private fun getGuessHintModel(guess: Array<CodePeg>): GuessHintModel {
            return GuessHintModel(
                number = "1",
                guess = guess,
                hint = List(4) {
                    KeyPeg(
                        KeyPegValue.EMPTY
                    )
                },
                isCurrentLevel = false,
                isGuessCorrect = false
            )
        }
    }

    @Before
    fun setUp() {
        cut = AreColorsInRightPositionsUseCase()
    }

    @Test
    fun `Given index, color and secret when execute then return if color is in right position`() {
        // Given

        // When
        val actual = cut.execute(guessHint, secret)

        // Then
        Assert.assertEquals(expected, actual)
    }
}