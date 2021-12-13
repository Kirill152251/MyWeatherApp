package com.example.myweatherapp.model.network.forecastResponse


import com.google.gson.annotations.SerializedName

data class ForecastItems(
    @SerializedName("dt_txt")
    val dtTxt: String,
    @SerializedName("main")
    val mainForecast: MainForecast,
    @SerializedName("weather")
    val weatherForecast: List<WeatherForecast>
)