package com.example.myweatherapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.model.network.CurrentWeatherResponse
import com.example.myweatherapp.model.network.api.OpenWeatherApi
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

    fun fetchCurrentWeather(location: String) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                openWeatherApi.getCurrentWeather(location)
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