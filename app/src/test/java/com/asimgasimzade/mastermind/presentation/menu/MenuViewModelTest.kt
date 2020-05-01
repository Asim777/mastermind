package com.asimgasimzade.mastermind.presentation.menu

import android.app.Application
import com.asimgasimzade.mastermind.framework.TestSchedulerProvider
import com.asimgasimzade.mastermind.presentation.menu.MenuViewModel.Companion.Destination
import com.asimgasimzade.mastermind.usecases.GetGameSettingsUseCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MenuViewModelTest {
    private lateinit var cut: MenuViewModel

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var getGameSettingsUseCase: GetGameSettingsUseCase

    @Before
    fun setUp() {
        cut = MenuViewModel(
            application,
            TestSchedulerProvider(),
            getGameSettingsUseCase
        )
    }

    @Test
    fun `When onNewGameClicked then navigate to Game`() {
        // Given
        val expectedDestination = Destination.NewGame
        val navigateObserver = cut.navigate().test()

        // When
        cut.onNewGameClicked()

        // Then
        navigateObserver
            .assertValue(expectedDestination)
            .assertNoErrors()
    }

    @Test
    fun `When onMultiPlayerClicked then navigate to Game`() {
        // Given
        val expectedDestination = Destination.MultiPlayer
        val navigateObserver = cut.navigate().test()

        // When
        cut.onMultiPlayerClicked()

        // Then
        navigateObserver
            .assertValue(expectedDestination)
            .assertNoErrors()
    }

    @Test
    fun `When onSettingsClicked then navigate to Settings`() {
        // Given
        val expectedDestination = Destination.Settings
        val navigateObserver = cut.navigate().test()

        // When
        cut.onSettingsClicked()

        // Then
        navigateObserver
            .assertValue(expectedDestination)
            .assertNoErrors()
    }

    @Test
    fun `When onHowToPlayClicked then navigate to Game`() {
        // Given
        val expectedDestination = Destination.HowToPlay
        val navigateObserver = cut.navigate().test()

        // When
        cut.onHowToPlayClicked()

        // Then
        navigateObserver
            .assertValue(expectedDestination)
            .assertNoErrors()
    }

    @Test
    fun `When onExitClicked then navigate to Exit`() {
        // Given
        val expectedDestination = Destination.Exit
        val navigateObserver = cut.navigate().test()

        // When
        cut.onExitClicked()

        // Then
        navigateObserver
            .assertValue(expectedDestination)
            .assertNoErrors()
    }
}