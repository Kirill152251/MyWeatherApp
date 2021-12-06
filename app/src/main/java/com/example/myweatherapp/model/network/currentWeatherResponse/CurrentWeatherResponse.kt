package com.example.myweatherapp.model.network.currentWeatherResponse


data class CurrentWeatherResponse(
    val main: Main,
    val name: String,
    val sys: Sys,
    val rain: Rain? = null,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind,
    val snow: Snow? = null
)