package com.example.myweatherapp.viewModels

import androidx.lifecycle.*
import com.example.myweatherapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun currentWeather(lat: Double, lon: Double) = repository.getCurrentWeather(lat, lon)

}