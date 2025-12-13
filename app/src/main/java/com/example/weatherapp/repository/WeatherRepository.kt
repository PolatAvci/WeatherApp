package com.example.weatherapp.repository

import com.example.weatherapp.api.WeatherApiService
import com.example.weatherapp.model.WeatherResponse

class WeatherRepository(private val api: WeatherApiService) {
    suspend fun fetchWeather(location: String, apiKey: String): WeatherResponse {
        return api.getWeather(location = location, apiKey = apiKey)
    }
}
