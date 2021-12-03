package com.example.myweatherapp.model.network



import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    val main: Main,
    val name: String,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)