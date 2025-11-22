package com.example.weatherapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.weatherapp.network.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.io.IOException

class WeatherDataRepository (
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val FORECAST_JSON_KEY = stringPreferencesKey("forecast_json")
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
    }

    private suspend fun saveForecastJson(json: String) {
        dataStore.edit { prefs ->
            prefs[FORECAST_JSON_KEY] = json
        }
    }

    suspend fun fetchAndSaveForecastJson(lon: Double, lat: Double): Result<String> {
        return try {
            val jsonResponse = RetrofitInstance.api.getForecast(lon, lat)
            saveForecastJson(jsonResponse)
            Result.success(jsonResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    val forecastJsonFlow: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { prefs ->
            prefs[FORECAST_JSON_KEY]
        }

    val coordinatesFlow: Flow<Pair<Double, Double>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { prefs ->
            val lat = prefs[LATITUDE]
            val lon = prefs[LONGITUDE]
            if (lat != null && lon != null) Pair(lat, lon) else null
        }
        .filterNotNull()

    suspend fun saveCoordinates(latitude: Double, longitude: Double) {
        dataStore.edit { prefs ->
            prefs[LATITUDE] = latitude
            prefs[LONGITUDE] = longitude
        }
    }
}