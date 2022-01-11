package com.example.myweatherapp.repository

import androidx.lifecycle.LiveData
import com.example.myweatherapp.model.network.currentWeatherResponse.CurrentWeatherResponse
import com.example.myweatherapp.model.network.api.OpenWeatherApi
import com.example.myweatherapp.model.network.forecastResponse.ForecastResponse
import com.google.android.gms.location.FusedLocationProviderClient
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val openWeatherApi: OpenWeatherApi) {

     lateinit var weatherNetworkDataSource: WeatherNetworkDataSource

    fun fetchCurrentWeather(
        fusedLocationProviderClient: FusedLocationProviderClient,
        compositeDisposable: CompositeDisposable
    ): LiveData<CurrentWeatherResponse> {
        weatherNetworkDataSource = WeatherNetworkDataSource(openWeatherApi, compositeDisposable)
        weatherNetworkDataSource.fetchCurrentWeather(fusedLocationProviderClient)
        return weatherNetworkDataSource.downloadedCurrentWeatherResponse
    }

    fun fetchForecast(
        fusedLocationProviderClient: FusedLocationProviderClient,
        compositeDisposable: CompositeDisposable
    ): LiveData<ForecastResponse> {
        weatherNetworkDataSource = WeatherNetworkDataSource(openWeatherApi, compositeDisposable)
        weatherNetworkDataSource.fetchForecast(fusedLocationProviderClient)
        return weatherNetworkDataSource.downloadedForecastResponse
    }

    fun fetchNetworkState(): LiveData<NetworkState> {
        return weatherNetworkDataSource.networkState
    }
}