package com.asimgasimzade.mastermind.framework.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.asimgasimzade.mastermind.framework.AppSchedulerProvider
import com.asimgasimzade.mastermind.framework.SchedulerProvider
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
}
