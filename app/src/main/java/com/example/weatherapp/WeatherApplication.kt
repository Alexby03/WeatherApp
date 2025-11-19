package com.example.weatherapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherapp.data.UserPreferencesRepository

private const val APP_PREFERENCES_NAME = "weather_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = APP_PREFERENCES_NAME
)

class WeatherApplication : Application() {

    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()

        userPreferencesRepository = UserPreferencesRepository(
            dataStore = this.dataStore
        )
    }
}