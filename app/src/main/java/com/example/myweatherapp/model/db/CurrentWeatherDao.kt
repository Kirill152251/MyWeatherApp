package com.example.myweatherapp.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myweatherapp.model.constants.Constants.CURRENT_WEATHER_ID
import com.example.myweatherapp.model.db.entities.CurrentWeatherEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather(weatherEntity: CurrentWeatherEntity): Completable

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getCurrentWeatherFromDB(): Single<CurrentWeatherEntity>
}