package com.example.myweatherapp.model.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myweatherapp.model.constants.Constants.CURRENT_WEATHER_ID
import com.example.myweatherapp.model.network.currentWeatherResponse.*

@Entity(tableName = "current_weather")
data class CurrentWeatherEntity(
    @Embedded(prefix = "main_")
    val main: Main,

    val name: String,

    @Embedded(prefix = "sys_")
    val sys: Sys,

    @Embedded(prefix = "rain_")
    val rain: Rain? = null,

    @Embedded(prefix = "snow_")
    val snow: Snow? = null,

    val visibility: Int,

    val weather: List<Weather>,

    @Embedded(prefix = "wind_")
    val wind: Wind,

    @PrimaryKey(autoGenerate = false)
    val id: Int = CURRENT_WEATHER_ID
)
