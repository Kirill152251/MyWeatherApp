package com.example.myweatherapp.model.network.forecastResponse


import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class ForecastItems(
    @SerializedName("dt_txt")
    val dtTxt: String,

    @SerializedName("main")
    @Embedded(prefix = "main_")
    val mainForecast: MainForecast,

    @SerializedName("weather")
    val weatherForecast: List<WeatherForecast>
)