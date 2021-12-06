package com.example.myweatherapp.model.network.currentWeatherResponse

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h1: Double? = null
)
