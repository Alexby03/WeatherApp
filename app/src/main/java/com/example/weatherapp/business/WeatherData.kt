package com.example.weatherapp.business

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class WeatherData(
    @SerialName("approvedTime")
    val ApprovedTime: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime,

    @SerialName("timeSeries")
    val series: List<SerieData>
)
