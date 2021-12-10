package com.example.myweatherapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.model.network.currentWeatherResponse.CurrentWeatherResponse
import com.example.myweatherapp.repository.NetworkState

import com.example.myweatherapp.repository.Repository
import io.reactivex.disposables.CompositeDisposable

class CurrentWeatherViewModel(
    private val repository: Repository,
    lat: Double, lon: Double
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val currentWeather: LiveData<CurrentWeatherResponse> by lazy {
        repository.fetchCurrentWeather(lat, lon, compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        repository.fetchNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}