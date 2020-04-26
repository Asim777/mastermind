package com.asimgasimzade.mastermind.framework.di

import com.asimgasimzade.mastermind.presentation.HomeActivity
import com.asimgasimzade.mastermind.presentation.HomeViewModel
import com.asimgasimzade.mastermind.presentation.game.GameFragment
import com.asimgasimzade.mastermind.presentation.game.GameViewModel
import com.asimgasimzade.mastermind.presentation.howtoplay.HowToPlayFragment
import com.asimgasimzade.mastermind.presentation.howtoplay.HowToPlayViewModel
import com.asimgasimzade.mastermind.presentation.howtoplay.howtoplayscreen.HowToPlayScreenFragment
import com.asimgasimzade.mastermind.presentation.howtoplay.howtoplayscreen.HowToPlayScreenViewModel
import com.asimgasimzade.mastermind.presentation.menu.MenuFragment
import com.asimgasimzade.mastermind.presentation.menu.MenuViewModel
import com.asimgasimzade.mastermind.presentation.settings.SettingsFragment
import com.asimgasimzade.mastermind.presentation.settings.SettingsViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class HomeActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    internal abstract fun bindGameFragment(): GameFragment

    @ContributesAndroidInjector
    internal abstract fun bindHowToPlayFragment(): HowToPlayFragment

    @ContributesAndroidInjector
    internal abstract fun bindHowToPlayScreenFragment(): HowToPlayScreenFragment

    @ContributesAndroidInjector
    internal abstract fun bindMenuFragment(): MenuFragment

    @ContributesAndroidInjector
    internal abstract fun bindSettingsFragment(): SettingsFragment

    @Module
    companion object {
        @Provides
        @JvmStatic
        internal fun provideHomeViewModelFactory(viewModel: HomeViewModel): ViewModelProviderFactory<HomeViewModel> {
            return ViewModelProviderFactory(viewModel)
        }

        @Provides
        @JvmStatic
        internal fun provideGameViewModelFactory(viewModel: GameViewModel): ViewModelProviderFactory<GameViewModel> {
            return ViewModelProviderFactory(viewModel)
        }

        @Provides
        @JvmStatic
        internal fun provideHowToPlayViewModelFactory(viewModel: HowToPlayViewModel): ViewModelProviderFactory<HowToPlayViewModel> {
            return ViewModelProviderFactory(viewModel)
        }

        @Provides
        @JvmStatic
        internal fun provideHowToPlayScreenViewModelFactory(viewModel: HowToPlayScreenViewModel): ViewModelProviderFactory<HowToPlayScreenViewModel> {
            return ViewModelProviderFactory(viewModel)
        }

        @Provides
        @JvmStatic
        internal fun provideMenuViewModelFactory(viewModel: MenuViewModel): ViewModelProviderFactory<MenuViewModel> {
            return ViewModelProviderFactory(viewModel)
        }

        @Provides
        @JvmStatic
        internal fun provideSettingsViewModelFactory(viewModel: SettingsViewModel): ViewModelProviderFactory<SettingsViewModel> {
            return ViewModelProviderFactory(viewModel)
        }
    }
}
