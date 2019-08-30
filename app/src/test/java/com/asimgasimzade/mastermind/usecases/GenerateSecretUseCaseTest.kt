package com.asimgasimzade.mastermind.usecases

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GenerateSecretUseCaseTest {
    private lateinit var cut: GenerateSecretUseCase

    @Before
    fun setUp() {
        cut = GenerateSecretUseCase()
    }

    @Test
    fun `Given duplicates allowed when execute then return secret of 4 non unique CodePegs`() {
        // When
        val actual = cut.execute(true)

        // Then
        actual.test()
            .assertValue { it.size == 4 }
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun `Given duplicates not allowed when execute then return secret of 4 unique CodePegs`() {
        // When
        val actual = cut.execute(false)

        // Then
        actual.test()
            .assertValue { it.size == 4 }
            .assertNever {
                it.containsDuplicate()
            }
            .assertComplete()
            .assertNoErrors()
    }
}

private fun <CodePeg> Array<CodePeg>.containsDuplicate() =
    this.groupingBy { it }.eachCount().values.any { it > 1 }
