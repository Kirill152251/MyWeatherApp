package com.example.myweatherapp.repository


import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.myweatherapp.model.db.CurrentWeatherDao
import com.example.myweatherapp.model.db.ForecastDao
import com.example.myweatherapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val forecastDao: ForecastDao
) {
    fun getCurrentWeather(lat: Double, lon: Double) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val lastUpdateTime = currentWeatherDao.getLastUpdateTime() ?: "firstInput"
        if (lastUpdateTime == "firstInput" || isUpdateWeatherNeeded(ZonedDateTime.parse(lastUpdateTime))) {
            val weatherFromApi = weatherNetworkDataSource.getCurrentWeatherFromApi(lat, lon)
            if (weatherFromApi.status == Resource.Status.SUCCESS) {
                weatherFromApi.data!!.updateTime = ZonedDateTime.now().toString()
                currentWeatherDao.insertCurrentWeather(weatherFromApi.data)
                val newWeatherFromDb =
                    currentWeatherDao.getCurrentWeatherFromDB().map { Resource.success(it) }
                emitSource(newWeatherFromDb)
            } else {
                emit(weatherFromApi) //will contain Resource.error
            }
        } else {
            emitSource(currentWeatherDao.getCurrentWeatherFromDB().map { Resource.success(it) } )
        }
    }

    fun getForecast(lat: Double, lon: Double) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val lastUpdateTime = forecastDao.getLastUpdateTime() ?: "firstInput"
        if (lastUpdateTime == "firstInput" || isUpdateWeatherNeeded(ZonedDateTime.parse(lastUpdateTime))) {
            val forecastFromApi = weatherNetworkDataSource.getForecastFromApi(lat, lon)
            if (forecastFromApi.status == Resource.Status.SUCCESS) {
                forecastFromApi.data!!.updateTime = ZonedDateTime.now().toString()
               forecastDao.insertForecast(forecastFromApi.data)
                val newForecastFromDb =
                    forecastDao.getForecastFromDb().map { Resource.success(it) }
                emitSource(newForecastFromDb)
            } else {
                emit(forecastFromApi) //will contain Resource.error
            }
        } else {
            emitSource(forecastDao.getForecastFromDb().map { Resource.success(it) } )
        }

    }

    //This function return true if last update was more when 60 min ago
    private fun isUpdateWeatherNeeded(lastUpdateTime: ZonedDateTime): Boolean {
        val tenMinutesAgo = ZonedDateTime.now().minusMinutes(60)
        return lastUpdateTime.isBefore(tenMinutesAgo)
    }

}