package com.example.myweatherapp.repository

import androidx.lifecycle.LiveData
import com.example.myweatherapp.model.network.CurrentWeatherResponse
import com.example.myweatherapp.model.network.api.OpenWeatherApi
import io.reactivex.disposables.CompositeDisposable

class Repository(private val openWeatherApi: OpenWeatherApi) {

    lateinit var weatherNetworkDataSource: WeatherNetworkDataSource

    fun fetchCurrentWeather(location: String, compositeDisposable: CompositeDisposable): LiveData<CurrentWeatherResponse> {
        weatherNetworkDataSource = WeatherNetworkDataSource(openWeatherApi, compositeDisposable)
        weatherNetworkDataSource.fetchCurrentWeather(location)
        return weatherNetworkDataSource.downloadedCurrentWeatherResponse
    }

    fun fetchNetworkState(): LiveData<NetworkState> {
        return weatherNetworkDataSource.networkState
    }
}