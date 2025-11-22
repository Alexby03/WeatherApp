package com.example.weatherapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.business.ApiModels
import com.example.weatherapp.business.ConnectivityObserver
import com.example.weatherapp.business.SerieData
import com.example.weatherapp.business.WeatherData
import com.example.weatherapp.business.toWeatherData
import com.example.weatherapp.data.WeatherDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import kotlin.String

interface WeatherViewModel {
    val weatherData: StateFlow<WeatherData>
    val latitudeInput: StateFlow<Double>
    val longitudeInput: StateFlow<Double>
    val isLoading: StateFlow<Boolean>
    val errorMessage: StateFlow<String?>
    val isOffline: StateFlow<Boolean>

    fun saveCoordinates(lat: Double, lon: Double)
    fun fetchWeather()
}

class WeatherVM (

    private val repository: WeatherDataRepository,
    private val connectivityObserver: ConnectivityObserver

): WeatherViewModel, ViewModel() {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val _weatherData = MutableStateFlow(
        WeatherData(
            ApprovedTime = LocalDateTime.now(),
            series = emptyList()
        )
    )
    override val weatherData: StateFlow<WeatherData>
        get() = _weatherData.asStateFlow()

    private var _latitudeInput = MutableStateFlow(0.0)
    override val latitudeInput: StateFlow<Double>
        get() = _latitudeInput.asStateFlow()

    private var _longitudeInput = MutableStateFlow(0.0)
    override val longitudeInput: StateFlow<Double>
        get() = _longitudeInput.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    override val errorMessage: StateFlow<String?>
        get() = _errorMessage.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    override val isOffline: StateFlow<Boolean>
        get() = _isOffline.asStateFlow()

    override fun saveCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.saveCoordinates(lat, lon)
            _latitudeInput.value = lat
            _longitudeInput.value = lon
            if (!_isOffline.value) fetchWeather()
        }
    }

    override fun fetchWeather() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.fetchAndSaveForecastJson(_longitudeInput.value, _latitudeInput.value)
            result.onSuccess { _ ->
            }.onFailure { error ->
                _errorMessage.value = "Failed to fetch: ${error.message}"
            }
            _isLoading.value = false
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WeatherApplication)
                WeatherVM(application.weatherDataRepository, connectivityObserver = application.connectivityObserver)
            }
        }
    }

    init {
        viewModelScope.launch {
            repository.coordinatesFlow.collect { (lat, lon) ->
                _latitudeInput.value = lat
                _longitudeInput.value = lon
            }
        }

        viewModelScope.launch {
            repository.forecastJsonFlow.collect { jsonString ->
                if (jsonString != null) {
                    try {
                        val apiResponse = json.decodeFromString<ApiModels.ApiWeatherResponse>(jsonString)
                        _weatherData.value = apiResponse.toWeatherData()
                    } catch (e: Exception) {
                        _errorMessage.value = "Parse error: ${e.message}"
                    }
                }
            }
        }

        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                val offline = status != ConnectivityObserver.Status.AVAILABLE
                _isOffline.value = offline
                if (!offline && _latitudeInput.value != 0.0 && _longitudeInput.value != 0.0) {
                    fetchWeather()
                }
            }
        }
    }
}

class FakeVM(

) : WeatherViewModel {

    private val _weatherData = MutableStateFlow(
        WeatherData(
            ApprovedTime = LocalDateTime.now(),
            series = listOf(
                SerieData(-3.4f, LocalDateTime.now(), 8, 0.2f, 2),
                SerieData(-3.1f, LocalDateTime.now(), 8, 0.2f, 2),
                SerieData(-2.4f, LocalDateTime.now(), 8, 0.1f, 2),
                SerieData(-1.8f, LocalDateTime.now(), 8, 0.1f, 2),
                SerieData(-1.7f, LocalDateTime.now(), 8, 0.1f, 2)
            )
        )
    )

    override val latitudeInput: StateFlow<Double>
        get() = MutableStateFlow(0.0)
    override val longitudeInput: StateFlow<Double>
        get() = MutableStateFlow(0.0)
    override val isLoading: StateFlow<Boolean>
        get() = MutableStateFlow(false)
    override val errorMessage: StateFlow<String?>
        get() = MutableStateFlow(null)
    override val isOffline: StateFlow<Boolean>
        get() = MutableStateFlow(false)

    override val weatherData: StateFlow<WeatherData> = _weatherData
    override fun saveCoordinates(lat: Double, lon: Double) { }
    override fun fetchWeather() { }

}
