package com.example.myweatherapp.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myweatherapp.model.network.forecastResponse.ForecastResponse
import com.google.gson.Gson

@Database(entities = [ForecastResponse::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ForecastDatabase : RoomDatabase() {

    abstract fun getForecastDao(): ForecastDao

    companion object {
        @Volatile private var instance: ForecastDatabase? = null

        fun getDatabase(context: Context): ForecastDatabase =
            instance?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it }}

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, ForecastDatabase::class.java, "forecast")
                .fallbackToDestructiveMigration()
                .addTypeConverter(Converters(GsonParser(Gson())))
                .build()
    }
}