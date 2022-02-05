package com.example.myweatherapp.repository

import com.example.myweatherapp.model.network.api.OpenWeatherApi
import com.example.myweatherapp.utils.BaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherNetworkDataSource @Inject constructor(
    private val openWeatherApi: OpenWeatherApi
): BaseDataSource() {

    suspend fun getCurrentWeatherFromApi(lat: Double, lon: Double) = getResult {
        openWeatherApi.getCurrentWeather(lat, lon)
    }

    suspend fun getForecastFromApi(lat: Double, lon: Double) = getResult {
        openWeatherApi.getForecast(lat, lon)
    }
}