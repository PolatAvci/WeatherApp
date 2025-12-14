package com.example.weatherapp.repository

import com.example.weatherapp.api.WeatherApiService
import com.example.weatherapp.model.WeatherResponse

class WeatherRepository(private val api: WeatherApiService) {
    suspend fun fetchWeather(location: String, apiKey: String, startDate: String? = null, endDate: String? = null): WeatherResponse {
        return if (!startDate.isNullOrBlank() && !endDate.isNullOrBlank()) {
            api.getWeatherWithDates(location, startDate, endDate, apiKey = apiKey)
        } else {
            api.getWeather(location, apiKey = apiKey)
        }
    }


}
