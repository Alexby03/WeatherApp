package com.example.weatherapp.business

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class SerieData(
    val temperature: Float,
    val captureTime: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime,
    val cloudDispersity: Int,
    val precipitation: Float,
    val weatherCode: Int
)
