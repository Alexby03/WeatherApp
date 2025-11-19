package com.example.weatherapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.data.UserPreferencesRepository
import kotlinx.coroutines.launch

interface WeatherViewModel {

}

class WeatherVM (
    private val repository: UserPreferencesRepository
): WeatherViewModel, ViewModel() {


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WeatherApplication)
                WeatherVM(application.userPreferencesRepository)
            }
        }
    }
    init {
        viewModelScope.launch {
            launch {
                //repository.highscore.collect { _highscore.value = it }
            }
        }
    }
}

class FakeVM : WeatherViewModel {

}