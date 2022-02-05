package com.example.myweatherapp.model.network.forecastResponse

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "forecast")
data class ForecastResponse(

    @SerializedName("list")
    val fiveDays: List<ForecastItems>,

    @Embedded(prefix = "city_")
    val city: City,

    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "updatedTime")
    var updateTime: String? = null
)