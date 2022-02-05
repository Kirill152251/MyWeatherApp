package com.example.myweatherapp.model.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.myweatherapp.model.network.currentWeatherResponse.Weather
import com.example.myweatherapp.model.network.forecastResponse.ForecastItems
import com.example.myweatherapp.model.network.forecastResponse.WeatherForecast
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters(private val jsonParser: JsonParser) {

    @TypeConverter
    fun fromWeatherJson(json: String): List<Weather> {
        return jsonParser.fromJson<ArrayList<Weather>>(
            json,
            object : TypeToken<ArrayList<Weather>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toWeatherJson(weather: List<Weather>): String {
        return jsonParser.toJson(
            weather,
            object : TypeToken<ArrayList<Weather>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromForecastItemsJson(json: String): List<ForecastItems> {
        return jsonParser.fromJson<ArrayList<ForecastItems>>(
            json,
            object : TypeToken<ArrayList<ForecastItems>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toForecastItemJson(forecastItems: List<ForecastItems>): String {
        return jsonParser.toJson(
            forecastItems,
            object : TypeToken<ArrayList<ForecastItems>>() {}.type
        ) ?: "[]"
    }
}