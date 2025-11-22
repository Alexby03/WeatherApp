package com.example.weatherapp.data

enum class WeatherType(val id: Int, val emoji: String) {
    CLEAR_SKY(1, "☀️"),
    NEARLY_CLEAR_SKY(2, "🌤️"),
    VARIABLE_CLOUDINESS(3, "⛅"),
    HALF_CLEAR_SKY(4, "🌥️"),
    CLOUDY_SKY(5, "☁️"),
    OVERCAST(6, "☁️"),
    FOG(7, "🌫️"),

    LIGHT_RAIN_SHOWERS(8, "🌦️"),
    MODERATE_RAIN_SHOWERS(9, "🌧️"),
    HEAVY_RAIN_SHOWERS(10, "⛈️🌧️"),

    THUNDERSTORM(11, "⛈️"),

    LIGHT_SLEET_SHOWERS(12, "🌧️❄️"),
    MODERATE_SLEET_SHOWERS(13, "🌧️❄️"),
    HEAVY_SLEET_SHOWERS(14, "🌧️❄️❄️"),

    LIGHT_SNOW_SHOWERS(15, "🌨️"),
    MODERATE_SNOW_SHOWERS(16, "🌨️"),
    HEAVY_SNOW_SHOWERS(17, "❄️🌨️"),

    LIGHT_RAIN(18, "🌧️"),
    MODERATE_RAIN(19, "🌧️🌦️"),
    HEAVY_RAIN(20, "🌧️🌧️"),

    THUNDER(21, "⚡"),

    LIGHT_SLEET(22, "🌧️❄️"),
    MODERATE_SLEET(23, "🌧️❄️"),
    HEAVY_SLEET(24, "🌧️❄️❄️"),

    LIGHT_SNOWFALL(25, "❄️"),
    MODERATE_SNOWFALL(26, "❄️🌨️"),
    HEAVY_SNOWFALL(27, "❄️❄️🌨️");

    companion object {
        fun fromId(id: Int): String {
            return WeatherType.entries.firstOrNull { it.id == id }?.emoji ?: "❓"
        }
    }
}
