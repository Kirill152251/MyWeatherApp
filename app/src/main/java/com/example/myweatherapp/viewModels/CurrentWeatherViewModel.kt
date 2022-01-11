package com.example.myweatherapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.model.network.currentWeatherResponse.CurrentWeatherResponse
import com.example.myweatherapp.repository.NetworkState
import com.example.myweatherapp.repository.Repository
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val repository: Repository,
    /*Via fusedLocationProviderClient i will get
    latitude and longitude in WeatherNetworkDatasource.
    After that lat and lon will be used in api response.*/
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {

    @Inject lateinit var compositeDisposable: CompositeDisposable

    val currentWeather: LiveData<CurrentWeatherResponse> by lazy {
        repository.fetchCurrentWeather(
            fusedLocationProviderClient,
            compositeDisposable
        )
    }
    val networkState: LiveData<NetworkState> by lazy {
        repository.fetchNetworkState()
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}