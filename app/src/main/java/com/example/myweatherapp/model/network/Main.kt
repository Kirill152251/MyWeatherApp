package com.example.myweatherapp.model.network


import com.google.gson.annotations.SerializedName

data class Main(
    val humidity: Int,
    val pressure: Int,
    val temp: Double
)