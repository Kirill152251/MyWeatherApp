package com.example.myweatherapp.model.network.currentWeatherResponse

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "current_weather")
data class CurrentWeatherResponse(
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

    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "updatedTime")
    var updateTime:String? = null
)
