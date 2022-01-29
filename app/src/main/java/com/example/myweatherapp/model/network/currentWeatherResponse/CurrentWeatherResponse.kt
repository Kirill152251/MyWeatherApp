package com.example.myweatherapp.model.network.currentWeatherResponse

import com.example.myweatherapp.model.db.entities.CurrentWeatherEntity


data class CurrentWeatherResponse(
    val main: Main,
    val name: String,
    val sys: Sys,
    val rain: Rain? = null,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind,
    val snow: Snow? = null
) {
    fun toCurrentWeatherEntity(): CurrentWeatherEntity {
        return CurrentWeatherEntity(
            main = main,
            name = name,
            sys = sys,
            rain = rain,
            snow = snow,
            visibility = visibility,
            weather = weather,
            wind = wind
        )
    }
}