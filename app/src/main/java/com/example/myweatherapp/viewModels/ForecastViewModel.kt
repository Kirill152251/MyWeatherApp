package com.example.myweatherapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.model.network.forecastResponse.ForecastResponse
import com.example.myweatherapp.repository.NetworkState
import com.example.myweatherapp.repository.Repository
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val repository: Repository,
    /**
     * Via fusedLocationProviderClient i get latitude and longitude for the WeatherNetworkDatasource in repository.
     * After that lat and lon are used in api response.
     */
    private val fusedLocationProviderClient: FusedLocationProviderClient
) :
    ViewModel() {
    @Inject lateinit var compositeDisposable: CompositeDisposable

    val forecast: LiveData<ForecastResponse> by lazy {
        repository.fetchForecast(fusedLocationProviderClient, compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        repository.fetchNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}