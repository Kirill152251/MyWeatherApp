package com.example.myweatherapp.model.network.currentWeatherResponse


data class Weather(
    val description: String,
    val icon: String,
    val main: String
)