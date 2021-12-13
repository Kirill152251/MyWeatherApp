package com.example.myweatherapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.model.network.forecastResponse.ForecastResponse
import com.example.myweatherapp.repository.NetworkState
import com.example.myweatherapp.repository.Repository
import io.reactivex.disposables.CompositeDisposable

class ForecastViewModel(private val repository: Repository, lat: Double, lon: Double) :
    ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val forecast: LiveData<ForecastResponse> by lazy {
        repository.fetchForecast(lat, lon, compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        repository.fetchNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}