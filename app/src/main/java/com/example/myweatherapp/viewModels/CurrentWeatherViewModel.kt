package com.example.myweatherapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.model.constants.Constants.LAT_KEY
import com.example.myweatherapp.model.constants.Constants.LON_KEY
import com.example.myweatherapp.model.network.currentWeatherResponse.CurrentWeatherResponse
import com.example.myweatherapp.repository.NetworkState

import com.example.myweatherapp.repository.Repository
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val repository: Repository,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : ViewModel() {
    //@Inject lateinit var compositeDisposable: CompositeDisposable
    private val compositeDisposable = CompositeDisposable()

//    private val _lat = savedStateHandle.getLiveData<Double>(LAT_KEY)
//    var lat: LiveData<Double> = _lat
//    private val _lon = savedStateHandle.getLiveData<Double>(LON_KEY)
//    var lon: LiveData<Double> = _lon

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