package com.example.weatherapp.model

data class WeatherResponse(
    val address: String,
    val days: List<DayWeather>
)
