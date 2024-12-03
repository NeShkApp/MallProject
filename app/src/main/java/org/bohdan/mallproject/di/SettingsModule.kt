package org.bohdan.mallproject.di

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import org.bohdan.mallproject.data.SettingsRepositoryImpl
import org.bohdan.mallproject.domain.repository.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    @Singleton
    fun provideSettingsRepository(
        sharedPreferences: SharedPreferences,
    ): SettingsRepository {
        return SettingsRepositoryImpl(
            sharedPreferences
        )
    }

}