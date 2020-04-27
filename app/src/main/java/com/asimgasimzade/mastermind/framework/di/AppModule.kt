package com.asimgasimzade.mastermind.framework.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.asimgasimzade.mastermind.data.gamedata.GameDataDataSource
import com.asimgasimzade.mastermind.data.gamedata.GameDataRepository
import com.asimgasimzade.mastermind.data.gamedata.GameDataRepositoryType
import com.asimgasimzade.mastermind.data.gamedata.LocalGameDataDataSource
import com.asimgasimzade.mastermind.data.gamesettings.GameSettingsDataSource
import com.asimgasimzade.mastermind.data.gamesettings.GameSettingsRepository
import com.asimgasimzade.mastermind.data.gamesettings.GameSettingsRepositoryType
import com.asimgasimzade.mastermind.data.gamesettings.LocalGameSettingsDataSource
import com.asimgasimzade.mastermind.framework.AppSchedulerProvider
import com.asimgasimzade.mastermind.framework.SchedulerProvider
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    fun provideResources(app: Application): Resources = app.resources

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

    @Provides
    @Reusable
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(
            "mastermind.preferences", Context.MODE_PRIVATE
        )

    @Provides
    @Reusable
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideGameSettingsRepository(
        dataSource: LocalGameSettingsDataSource
    ) = GameSettingsRepository(dataSource)

    @Provides
    @Reusable
    fun provideLocalGameSettingsDataSource(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): GameSettingsDataSource =
        LocalGameSettingsDataSource(
            sharedPreferences,
            gson
        )

    @Provides
    @Singleton
    fun provideGameDataRepository(
        dataSource: LocalGameDataDataSource
    ) = GameDataRepository(
        dataSource
    )

    @Provides
    @Reusable
    fun provideLocalGameDataDataSource(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): GameDataDataSource =
        LocalGameDataDataSource(
            sharedPreferences,
            gson
        )
}
