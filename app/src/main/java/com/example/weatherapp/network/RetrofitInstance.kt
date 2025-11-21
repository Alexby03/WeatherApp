package com.example.weatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://opendata-download-metfcst.smhi.se"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    val api: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}
