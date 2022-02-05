package com.example.myweatherapp.di

import com.example.myweatherapp.model.db.CurrentWeatherDao
import com.example.myweatherapp.model.db.ForecastDao
import com.example.myweatherapp.model.network.api.OpenWeatherApi
import com.example.myweatherapp.repository.Repository
import com.example.myweatherapp.repository.WeatherNetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    private const val API_KEY = "98c8b6b22e063675fb3b8a71a56e5c7b"
    private const val UNITS = "metric"
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    @Singleton
    @Provides
    fun provideRequestInterceptor(): Interceptor {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("appid", API_KEY)
                .addQueryParameter("units", UNITS)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }
        return requestInterceptor
    }

    @Singleton
    @Provides
    fun provideClient(requestInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): OpenWeatherApi =
        retrofit.create(OpenWeatherApi::class.java)

    @Singleton
    @Provides
    fun provideRepository(
        currentWeatherDAO: CurrentWeatherDao,
        weatherNetworkDataSource: WeatherNetworkDataSource,
        forecastDao: ForecastDao,
    ) = Repository(currentWeatherDAO, weatherNetworkDataSource, forecastDao)
}