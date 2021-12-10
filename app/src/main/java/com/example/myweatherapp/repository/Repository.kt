package com.example.myweatherapp.repository

import androidx.lifecycle.LiveData
import com.example.myweatherapp.model.network.currentWeatherResponse.CurrentWeatherResponse
import com.example.myweatherapp.model.network.api.OpenWeatherApi
import io.reactivex.disposables.CompositeDisposable

class Repository(private val openWeatherApi: OpenWeatherApi) {

    lateinit var weatherNetworkDataSource: WeatherNetworkDataSource

    fun fetchCurrentWeather(lat:  Double, lon: Double, compositeDisposable: CompositeDisposable): LiveData<CurrentWeatherResponse> {
        weatherNetworkDataSource = WeatherNetworkDataSource(openWeatherApi, compositeDisposable)
        weatherNetworkDataSource.fetchCurrentWeather(lat, lon)
        return weatherNetworkDataSource.downloadedCurrentWeatherResponse
    }

    fun fetchNetworkState(): LiveData<NetworkState> {
        return weatherNetworkDataSource.networkState
    }
}