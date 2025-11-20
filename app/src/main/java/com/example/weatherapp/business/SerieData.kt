package com.example.weatherapp.business

import java.time.LocalDateTime

data class SerieData(
    val temperature: Float,
    val captureTime: LocalDateTime,
    val cloudDispersity: Int,
    val precipitation: Float,
    val weatherCode: Int
)