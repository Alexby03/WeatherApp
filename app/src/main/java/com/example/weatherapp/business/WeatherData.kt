package com.example.weatherapp.business

import java.time.LocalDateTime

data class WeatherData(
    val ApprovedTime: LocalDateTime,
    val series: List<SerieData>
)