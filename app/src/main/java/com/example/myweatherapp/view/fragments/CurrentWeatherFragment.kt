package com.example.myweatherapp.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.myweatherapp.model.network.CurrentWeatherResponse
import com.example.myweatherapp.model.network.api.ICON_URL
import com.example.myweatherapp.model.network.api.OpenWeatherApi
import com.example.myweatherapp.model.network.api.WeatherClient
import com.example.myweatherapp.repository.NetworkState
import com.example.myweatherapp.repository.Repository
import com.example.myweatherapp.viewModels.CurrentWeatherViewModel
import com.example.myweatherapp.viewModels.CurrentWeatherViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.floor


class CurrentWeatherFragment : Fragment(R.layout.fragment_current_weather) {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var viewModelFactory: CurrentWeatherViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weatherApi: OpenWeatherApi = WeatherClient.getClient()
        val repository = Repository(weatherApi)
        viewModelFactory = CurrentWeatherViewModelFactory(repository, "Minsk")
        viewModel = ViewModelProvider(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)
        viewModel.currentWeather.observe(viewLifecycleOwner, Observer {
            bindUI(it)
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            binding.currentWeatherProgressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.cwErrorMsg.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

    }

    @SuppressLint("SetTextI18n", "CheckResult")
    private fun bindUI(it: CurrentWeatherResponse) {
        binding.windSpeedValue.text = it.wind.speed.toString() + " km/h"
        val weather = it.weather.first()
        binding.currentTemp.text = it.main.temp.toInt().toString() + "°C" + " | " + weather.main
        binding.currentLocation.text = it.name + ", " + it.sys.country
        binding.humidityValue.text = it.main.humidity.toString() + "%"
        binding.pressureValue.text = it.main.pressure.toString() + " hPa"
        binding.windDirectionValue.text = getWindDirection(it.wind.deg)

        if (it.rain?.h1 == null) {
            binding.rainValue.text = "0.0 mm"
        } else {
            binding.rainValue.text = it.rain.h1.toString() + " mm"
        }

        val iconUrl = ICON_URL + weather.icon + "@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(binding.currentWeatherImage)
        Log.i("IconInfo", iconUrl)
    }

    private fun getWindDirection(degrees: Int):String{
        val array = listOf("N","NNE","NE","ENE","E","ESE", "SE", "SSE","S","SSW","SW","WSW","W","WNW","NW","NNW")
        val value = floor((degrees / 22.5) + 0.5)
        return array[(value%16).toInt()]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}