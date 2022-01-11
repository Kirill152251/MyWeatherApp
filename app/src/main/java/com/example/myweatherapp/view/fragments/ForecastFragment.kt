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
import com.example.myweatherapp.repository.NetworkState
import com.example.myweatherapp.repository.Repository
import com.example.myweatherapp.view.ForecastAdapter
import com.example.myweatherapp.viewModels.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var repository: Repository
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

        viewModel.forecast.observe(viewLifecycleOwner, Observer { forecast ->
            binding.location.text = forecast.city.name
            val adapter = ForecastAdapter(forecast, requireContext())
            binding.rvForecast.layoutManager = LinearLayoutManager(requireContext())
            binding.rvForecast.adapter = adapter
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer { currentNetworkState ->
            if (currentNetworkState == NetworkState.LOADING) {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvForecast.visibility = View.GONE
                binding.location.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.rvForecast.visibility = View.VISIBLE
                binding.location.visibility = View.VISIBLE
            }
            if (currentNetworkState == NetworkState.ERROR) {
                binding.errorMsg.visibility = View.VISIBLE
                binding.rvForecast.visibility = View.GONE
                binding.location.visibility = View.GONE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}