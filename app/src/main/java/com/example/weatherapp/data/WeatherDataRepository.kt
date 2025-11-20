package com.example.weatherapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class WeatherDataRepository (
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val FORECAST_JSON_KEY = stringPreferencesKey("forecast_json")
    }

    suspend fun saveForecastJson(json: String) {
        dataStore.edit { prefs ->
            prefs[FORECAST_JSON_KEY] = json
        }
    }

    val forecastJsonFlow: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { prefs ->
            prefs[FORECAST_JSON_KEY]
        }

}