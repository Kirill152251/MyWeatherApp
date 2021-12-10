package com.example.myweatherapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.repository.Repository

class CurrentWeatherViewModelFactory (private val repository: Repository, private val lat: Double, private val lon: Double): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(repository, lat, lon) as T
    }
}