package com.example.myweatherapp.model.network


import com.google.gson.annotations.SerializedName

data class Wind(
    val deg: Int,
    val speed: Double
)