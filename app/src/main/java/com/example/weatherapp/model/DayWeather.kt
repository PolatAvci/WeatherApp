package com.example.weatherapp.model

data class DayWeather(
    val datetime: String,
    val tempmax: Double,
    val tempmin: Double,
    val conditions: String
)
