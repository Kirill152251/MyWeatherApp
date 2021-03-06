package com.example.myweatherapp.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myweatherapp.model.network.currentWeatherResponse.CurrentWeatherResponse
import com.google.gson.Gson

@Database(entities = [CurrentWeatherResponse::class], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CurrentWeatherDatabase : RoomDatabase() {

    abstract fun getCurrentWeatherDao(): CurrentWeatherDao

    companion object {
        @Volatile private var instance: CurrentWeatherDatabase? = null

        fun getDatabase(context: Context): CurrentWeatherDatabase =
            instance?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it }}

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, CurrentWeatherDatabase::class.java, "current_weather")
                .fallbackToDestructiveMigration()
                .addTypeConverter(Converters(GsonParser(Gson())))
                .build()
    }
}