package com.example.myweatherapp.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.myweatherapp.databinding.ForecastItemBinding
import com.example.myweatherapp.databinding.ForecastItemFirstOfAdayBinding
import com.example.myweatherapp.model.constants.Constants.ICON_URL
import com.example.myweatherapp.model.network.forecastResponse.ForecastResponse
import java.util.*

sealed class ForecastRecyclerViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    class ForecastViewHolder(private val binding: ForecastItemBinding) :
        ForecastRecyclerViewHolder(binding) {
        @SuppressLint("SetTextI18n")
        fun bind(forecastResponse: ForecastResponse, position: Int, context: Context) {
            binding.tempValue.text =
                forecastResponse.list[position].mainForecast.temp.toInt().toString() + "°C"
            binding.weatherDescription.text =
                forecastResponse.list[position].weatherForecast.first().desc

            val time = forecastResponse.list[position].dtTxt.substringAfter(" ")
            val timeWithoutSecond = time.substringBeforeLast(":")
            binding.time.text = timeWithoutSecond


            val iconUrl =
                ICON_URL + forecastResponse.list[position].weatherForecast.first().icon + "@2x.png"
            Glide.with(context).load(iconUrl).into(binding.weatherImage)
        }
    }

    class FirstInADayForecastViewHolder(private val binding: ForecastItemFirstOfAdayBinding) :
        ForecastRecyclerViewHolder(binding) {
        @SuppressLint("SetTextI18n")
        fun bind(forecastResponse: ForecastResponse, position: Int, context: Context) {
            binding.tempValue.text =
                forecastResponse.list[position].mainForecast.temp.toInt().toString() + "°C"
            binding.weatherDescription.text =
                forecastResponse.list[position].weatherForecast.first().desc

            val time = forecastResponse.list[position].dtTxt.substringAfter(" ")
            val timeWithoutSecond = time.substringBeforeLast(":")
            binding.time.text = timeWithoutSecond

            val iconUrl =
                ICON_URL + forecastResponse.list[position].weatherForecast.first().icon + "@2x.png"
            Glide.with(context).load(iconUrl).into(binding.weatherImage)

            val date = forecastResponse.list[position].dtTxt.substringBefore(" ")
            //2021-12-20 12:00:00
            val day = date.substringAfterLast("-").toInt()
            val month = date.substringBeforeLast("-").substringAfterLast("-").toInt()
            val year = date.substringBefore("-").toInt()
            val c: Calendar = Calendar.getInstance()
            c.set(year,month,day)
            when(c.get(Calendar.DAY_OF_WEEK)) {
                1 -> binding.dayOfTheWeek.text = "THURSDAY"
                2 -> binding.dayOfTheWeek.text = "FRIDAY"
                3 -> binding.dayOfTheWeek.text = "SATURDAY"
                4 -> binding.dayOfTheWeek.text = "SUNDAY"
                5 -> binding.dayOfTheWeek.text = "MONDAY"
                6 -> binding.dayOfTheWeek.text = "TUESDAY"
                7 -> binding.dayOfTheWeek.text = "WEDNESDAY"
            }
        }
    }
}
