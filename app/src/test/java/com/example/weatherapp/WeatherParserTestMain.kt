package com.example.weatherapp

import com.example.weatherapp.business.WeatherParser

fun main() {
    val parser = WeatherParser()
    val weather = parser.parseWeather()

    println("----- WEATHER TEST -----")
    println("Approved time: ${weather.ApprovedTime}")
    println("Temperature: ${weather.series.first().temperature}")
    println("Clouds: ${weather.series.first().cloudDispersity}")
    println("Precipitation: ${weather.series.first().precipitation}")
    println("Weather code: ${weather.series.first().weatherCode}")
    println("Capture time: ${weather.series.first().captureTime}")
}
