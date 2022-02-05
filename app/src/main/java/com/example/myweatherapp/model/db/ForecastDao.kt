package com.example.myweatherapp.model.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myweatherapp.model.network.forecastResponse.ForecastResponse

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecastResponse: ForecastResponse)

    @Query("select * from forecast")
    fun getForecastFromDb(): LiveData<ForecastResponse>

    @Query("select `updatedTime` from forecast")
    fun getLastUpdateTime(): String

}