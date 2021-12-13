package com.example.myweatherapp.model.network.forecastResponse


import com.google.gson.annotations.SerializedName

data class WeatherForecast(
    val icon: String,
    val id: Int,
    val main: String
)