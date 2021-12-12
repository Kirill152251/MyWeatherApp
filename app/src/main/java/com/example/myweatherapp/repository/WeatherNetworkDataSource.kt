package com.example.myweatherapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.model.network.currentWeatherResponse.CurrentWeatherResponse
import com.example.myweatherapp.model.network.api.OpenWeatherApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class WeatherNetworkDataSource(
    private val openWeatherApi: OpenWeatherApi,
    private val compositeDisposable: CompositeDisposable
) {
    private val _downloadedCurrentWeatherResponse = MutableLiveData<CurrentWeatherResponse>()
    val downloadedCurrentWeatherResponse: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeatherResponse

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState


    fun fetchCurrentWeather(lat:  Double, lon: Double) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(

                openWeatherApi.getCurrentWeather(lat, lon)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedCurrentWeatherResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)

                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("NetworkDataSource", it.message!!)

                        })
            )
        } catch (e: Exception) {
            Log.e("NetworkDataSource", e.message!!)
        }
    }
}