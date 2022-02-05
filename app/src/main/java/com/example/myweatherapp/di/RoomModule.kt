package com.example.myweatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.myweatherapp.model.db.*
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideCurrentWeatherDatabase(@ApplicationContext context: Context) =
        CurrentWeatherDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideCurrentWeatherDao(currentWeatherDatabase: CurrentWeatherDatabase) =
        currentWeatherDatabase.getCurrentWeatherDao()

    @Singleton
    @Provides
    fun provideForecastDatabase(@ApplicationContext context: Context) =
        ForecastDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideForecastDao(forecastDatabase: ForecastDatabase) =
        forecastDatabase.getForecastDao()
}