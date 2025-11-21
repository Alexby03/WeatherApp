package com.example.weatherapp.network

import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApiService {
    @GET("api/category/pmp3g/version/2/geotype/point/lon/{lon}/lat/{lat}/data.json")
    suspend fun getForecast(
        @Path("lon") lon: Double,
        @Path("lat") lat: Double
    ): String
}
