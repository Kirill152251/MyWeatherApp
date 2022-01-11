package com.example.myweatherapp.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.myweatherapp.model.constants.Constants.ICON_URL
import com.example.myweatherapp.model.constants.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.example.myweatherapp.model.network.currentWeatherResponse.CurrentWeatherResponse
import com.example.myweatherapp.permissions.TrackingUtility
import com.example.myweatherapp.repository.NetworkState
import com.example.myweatherapp.repository.Repository
import com.example.myweatherapp.viewModels.CurrentWeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject
import kotlin.math.floor

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment(R.layout.fragment_current_weather),
    EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var repository: Repository
    private val viewModel by viewModels<CurrentWeatherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            showUI()
        } else {
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun showUI() {
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
    private fun bindUI(currentWeatherResponse: CurrentWeatherResponse) {
        binding.windSpeedValue.text = currentWeatherResponse.wind.speed.toString() + " km/h"
        val weather = currentWeatherResponse.weather.first()
        binding.currentTemp.text =
            currentWeatherResponse.main.temp.toInt().toString() + "°C" + " | " + weather.main
        binding.currentLocation.text =
            currentWeatherResponse.name + ", " + currentWeatherResponse.sys.country
        binding.humidityValue.text = currentWeatherResponse.main.humidity.toString() + "%"
        binding.pressureValue.text = currentWeatherResponse.main.pressure.toString() + " hPa"
        binding.windDirectionValue.text = getWindDirection(currentWeatherResponse.wind.deg)

        if (currentWeatherResponse.rain?.h1 == null) {
            if (currentWeatherResponse.snow?.h1 == null) {
                binding.rainImage.setImageResource(R.drawable.weather_rainy)
                binding.rainValue.text = "0.0 mm"
            } else {
                binding.rainImage.setImageResource(R.drawable.weather_snowy)
                binding.rainValue.text = currentWeatherResponse.snow.h1.toString() + " mm"
            }
        } else {
            binding.rainImage.setImageResource(R.drawable.weather_rainy)
            binding.rainValue.text = currentWeatherResponse.rain.h1.toString() + " mm"
        }

        val iconUrl = ICON_URL + weather.icon + "@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(binding.currentWeatherImage)

        binding.btnShare.setOnClickListener {
            share(
                "Weather type: ${weather.main}. Temperature: ${
                    currentWeatherResponse.main.temp.toInt().toString() + "°C."
                }"
            )
        }
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

    private fun share(data: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, data)
        startActivity(Intent.createChooser(intent, "Share"))
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