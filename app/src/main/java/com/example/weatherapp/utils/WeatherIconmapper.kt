package com.example.weatherapp.utils

import androidx.annotation.DrawableRes
import com.example.weatherapp.R

@DrawableRes
fun getWeatherIcon(condition: String): Int {
    val c = condition.lowercase()

    return when {
        c.contains("sun") || c.contains("clear") -> R.drawable.sunny
        c.contains("cloud") && c.contains("part") -> R.drawable.partialy_cloudy
        c.contains("cloud") -> R.drawable.cloudy
        c.contains("rain") && c.contains("snow") -> R.drawable.sonwy_rainy
        c.contains("rain") -> R.drawable.rainy
        c.contains("snow") -> R.drawable.snowy
        c.contains("fog") || c.contains("mist") -> R.drawable.foggy
        c.contains("wind") -> R.drawable.windy
        c.contains("thunder") || c.contains("lightning") -> R.drawable.lightning
        else -> R.drawable.cloudy
    }
}
