package com.example.myweatherapp.model.network.forecastResponse


import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val list: List<ForecastItems>,
    val city: City
)