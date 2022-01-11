package com.example.myweatherapp.model.network.api

import com.example.myweatherapp.model.network.currentWeatherResponse.CurrentWeatherResponse
import com.example.myweatherapp.model.network.forecastResponse.ForecastResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


/*
https://api.openweathermap.org/data/2.5/weather?q=Minsk&appid=98c8b6b22e063675fb3b8a71a56e5c7b
api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
https://api.openweathermap.org/data/2.5/forecast?lat=35&lon=139&appid=98c8b6b22e063675fb3b8a71a56e5c7b&units=metric
*/
interface OpenWeatherApi {
    @GET("weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Single<CurrentWeatherResponse>

    @GET("forecast")
    fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Single<ForecastResponse>
}