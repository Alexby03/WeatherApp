package com.example.weatherapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.ui.viewmodels.FakeVM
import com.example.weatherapp.ui.viewmodels.WeatherVM
import com.example.weatherapp.ui.viewmodels.WeatherViewModel

@Composable
fun WeatherScreen(
    vm: WeatherViewModel,
    modifier: Modifier = Modifier
) {

}

@Preview
@Composable
fun WeatherScreenPreview() {
    WeatherScreen(FakeVM(), modifier = Modifier.fillMaxSize(),)
}