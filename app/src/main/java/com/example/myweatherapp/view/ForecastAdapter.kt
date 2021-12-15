package com.example.myweatherapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.databinding.ForecastItemBinding
import com.example.myweatherapp.databinding.ForecastItemFirstOfAdayBinding

import com.example.myweatherapp.model.network.forecastResponse.ForecastResponse


class ForecastAdapter(private val forecastResponse: ForecastResponse, private val context: Context) :
    RecyclerView.Adapter<ForecastRecyclerViewHolder>() {
    private val FIRST_ITEM = 1
    private val ANOTHER_ITEM = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastRecyclerViewHolder {
        return when (viewType) {
            FIRST_ITEM -> {
                ForecastRecyclerViewHolder.FirstInADayForecastViewHolder(
                    ForecastItemFirstOfAdayBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                 ForecastRecyclerViewHolder.ForecastViewHolder(
                     ForecastItemBinding.inflate(
                         LayoutInflater.from(parent.context),
                         parent,
                         false
                     )
                 )
            }
        }
    }

    override fun onBindViewHolder(holder: ForecastRecyclerViewHolder, position: Int) {
        when (holder) {
            is ForecastRecyclerViewHolder.FirstInADayForecastViewHolder -> {
                holder.bind(forecastResponse, position, context)
            }
            is ForecastRecyclerViewHolder.ForecastViewHolder -> {
                holder.bind(forecastResponse,position,context)
            }
        }

    }

    override fun getItemCount() = forecastResponse.list.size

    override fun getItemViewType(position: Int): Int {
        return if (checkIfItemFirstInADay(position))
            FIRST_ITEM
        else ANOTHER_ITEM
    }

    private fun checkIfItemFirstInADay(position: Int): Boolean {
        val time = forecastResponse.list[position].dtTxt.substringAfter(" ")
        return position == 0 || time == "00:00:00"
    }
}