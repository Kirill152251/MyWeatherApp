package com.example.myweatherapp.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentForecastBinding
import com.example.myweatherapp.repository.Repository
import com.example.myweatherapp.utils.Resource
import com.example.myweatherapp.view.ForecastAdapter
import com.example.myweatherapp.viewModels.ForecastViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var repository: Repository
    @Inject lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel by viewModels<ForecastViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            viewModel.getForecast(it.latitude, it.longitude).observe(viewLifecycleOwner, Observer { forecast ->
                when (forecast.status) {
                    Resource.Status.SUCCESS -> {
                        val adapter = ForecastAdapter(forecast.data!!, requireContext())
                        binding.apply {
                            location.text = forecast.data.city.name
                            rvForecast.layoutManager = LinearLayoutManager(requireContext())
                            rvForecast.adapter = adapter
                            rvForecast.visibility = View.VISIBLE
                            location.visibility = View.VISIBLE
                            errorMsg.visibility = View.GONE
                            progressBar.visibility = View.GONE
                        }
                    }
                    Resource.Status.LOADING -> {
                        binding.apply {
                            progressBar.visibility = View.VISIBLE
                            rvForecast.visibility = View.GONE
                            location.visibility = View.GONE
                            errorMsg.visibility = View.GONE
                        }
                    }
                    Resource.Status.ERROR -> {
                        binding.apply {
                            progressBar.visibility = View.GONE
                            rvForecast.visibility = View.GONE
                            location.visibility = View.GONE
                            errorMsg.text = forecast.message
                            errorMsg.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}