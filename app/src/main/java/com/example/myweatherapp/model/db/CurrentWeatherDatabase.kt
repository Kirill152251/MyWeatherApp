package com.example.myweatherapp.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myweatherapp.model.db.entities.CurrentWeatherEntity

@Database(entities = [CurrentWeatherEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CurrentWeatherDatabase : RoomDatabase() {

    abstract fun getCurrentWeatherDao(): CurrentWeatherDao
}