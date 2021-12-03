package com.example.myweatherapp.model.network.api

import com.example.myweatherapp.model.network.CurrentWeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


//https://api.openweathermap.org/data/2.5/weather?q=Minsk&appid=98c8b6b22e063675fb3b8a71a56e5c7b
interface OpenWeatherApi {
    @GET("weather")
    fun getCurrentWeather(
        @Query("q") location: String
    ): Single<CurrentWeatherResponse>
}