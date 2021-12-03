package com.example.myweatherapp.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.myweatherapp.model.network.api.OpenWeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CurrentWeatherFragment : Fragment(R.layout.fragment_current_weather) {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val api = OpenWeatherApi()
        lifecycleScope.launch(Dispatchers.Main) {
            val currentWeatherResponse = api.getCurrentWeather("Minsk").await()
            binding.humidityValue.text = currentWeatherResponse.main.humidity.toString() + " %"
            binding.windSpeedValue.text = currentWeatherResponse.wind.speed.toString() + " km/h"
            binding.pressureValue.text = currentWeatherResponse.main.pressure.toString() + " hPa"
//            if (currentWeatherResponse.rain.h1 == null) {
//                binding.rainValue.text = "0 mm"
//            } else {
//                binding.rainValue.text = currentWeatherResponse.rain.h1.toString() + " mm"
//            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}