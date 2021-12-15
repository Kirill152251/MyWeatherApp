package com.example.myweatherapp.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.myweatherapp.model.constants.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.example.myweatherapp.model.network.currentWeatherResponse.CurrentWeatherResponse
import com.example.myweatherapp.model.network.api.ICON_URL
import com.example.myweatherapp.model.network.api.OpenWeatherApi
import com.example.myweatherapp.model.network.api.WeatherClient
import com.example.myweatherapp.permissions.TrackingUtility
import com.example.myweatherapp.repository.NetworkState
import com.example.myweatherapp.repository.Repository
import com.example.myweatherapp.viewModels.CurrentWeatherViewModel
import com.example.myweatherapp.viewModels.CurrentWeatherViewModelFactory
import com.google.android.gms.location.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit
import kotlin.math.floor


class CurrentWeatherFragment : Fragment(R.layout.fragment_current_weather),
    EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var viewModelFactory: CurrentWeatherViewModelFactory
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var repository: Repository
    private lateinit var locationRequest: LocationRequest


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weatherApi: OpenWeatherApi = WeatherClient.getClient()
        repository = Repository(weatherApi)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            showUI()
        } else {
            requestPermission()
        }

    }

    @SuppressLint("MissingPermission")
    private fun showUI() {

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it == null) {
                getNewLocation()
                showUI()

            } else {
                viewModelFactory =
                    CurrentWeatherViewModelFactory(repository, it.latitude, it.longitude)
                viewModel =
                    ViewModelProvider(
                        this,
                        viewModelFactory
                    ).get(CurrentWeatherViewModel::class.java)

                viewModel.currentWeather.observe(viewLifecycleOwner, Observer { weather ->
                    bindUI(weather)
                })
                viewModel.networkState.observe(viewLifecycleOwner, Observer { currentNetworkState ->
                    if (currentNetworkState == NetworkState.LOADING) {
                        binding.currentWeatherProgressBar.visibility = View.VISIBLE
                        binding.allUi.visibility = View.GONE
                    } else {
                        binding.currentWeatherProgressBar.visibility = View.GONE
                        binding.allUi.visibility = View.VISIBLE
                    }
                    if (currentNetworkState == NetworkState.ERROR) {
                        binding.cwErrorMsg.visibility = View.VISIBLE
                        binding.allUi.visibility = View.GONE
                    }

                })
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation(){
        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(0)
            fastestInterval = TimeUnit.SECONDS.toMillis(0)
            maxWaitTime = TimeUnit.SECONDS.toMillis(5)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, callback, Looper.myLooper()
        )
    }
    private val callback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
        }
    }

    private fun requestPermission() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        EasyPermissions.requestPermissions(
            this,
            "You need to accept location permissions to use this app.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
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
            if (it.snow?.h1 == null) {
                binding.rainImage.setImageResource(R.drawable.weather_rainy)
                binding.rainValue.text = "0.0 mm"
            } else {
                binding.rainImage.setImageResource(R.drawable.weather_snowy)
                binding.rainValue.text = it.snow.h1.toString() + " mm"
            }
        } else {
            binding.rainImage.setImageResource(R.drawable.weather_rainy)
            binding.rainValue.text = it.rain.h1.toString() + " mm"
        }

        val iconUrl = ICON_URL + weather.icon + "@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(binding.currentWeatherImage)
    }

    private fun getWindDirection(degrees: Int): String {
        val array = listOf(
            "N",
            "NNE",
            "NE",
            "ENE",
            "E",
            "ESE",
            "SE",
            "SSE",
            "S",
            "SSW",
            "SW",
            "WSW",
            "W",
            "WNW",
            "NW",
            "NNW"
        )
        val value = floor((degrees / 22.5) + 0.5)
        return array[(value % 16).toInt()]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        showUI()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}