package com.example.myweatherapp.model.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.myweatherapp.model.network.currentWeatherResponse.Weather
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
}