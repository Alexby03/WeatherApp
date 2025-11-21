package com.example.weatherapp.business

import com.example.weatherapp.business.ApiModels.ApiTimeSeries
import com.example.weatherapp.business.ApiModels.ApiWeatherResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

class ApiModels {

    @Serializable
    data class ApiParameter(
        @SerialName("name")
        val name: String,

        @SerialName("values")
        val values: List<Float>
    )
    @Serializable
    data class ApiTimeSeries(
        @SerialName("validTime")
        val validTime: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime,

        @SerialName("parameters")
        val parameters: List<ApiParameter>
    )

    @Serializable
    data class ApiWeatherResponse(
        @SerialName("approvedTime")
        val approvedTime: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime,

        @SerialName("timeSeries")
        val timeSeries: List<ApiTimeSeries>
    )
}

fun ApiTimeSeries.toSerieData(): SerieData {
    fun getValue(name: String): Float {
        return parameters.firstOrNull { it.name == name }
            ?.values?.firstOrNull() ?: 0f
    }

    return SerieData(
        temperature = getValue("t"),
        captureTime = validTime,
        cloudDispersity = getValue("tcc_mean").toInt(),
        precipitation = getValue("pmean"),
        weatherCode = getValue("Wsymb2").toInt()
    )
}

fun ApiWeatherResponse.toWeatherData(): WeatherData {
    return WeatherData(
        ApprovedTime = this.approvedTime,
        series = this.timeSeries.map { it.toSerieData() }
    )
}