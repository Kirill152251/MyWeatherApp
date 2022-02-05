package com.example.myweatherapp.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myweatherapp.model.network.currentWeatherResponse.CurrentWeatherResponse


@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(CurrentWeatherResponse: CurrentWeatherResponse)

    @Query("select * from current_weather")
    fun getCurrentWeatherFromDB(): LiveData<CurrentWeatherResponse>

    @Query("select `updatedTime` from current_weather")
    fun getLastUpdateTime(): String
}