package com.example.myweatherapp.viewModels

import androidx.lifecycle.ViewModel
import com.example.myweatherapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getForecast(lat: Double, lon: Double) = repository.getForecast(lat, lon)

}