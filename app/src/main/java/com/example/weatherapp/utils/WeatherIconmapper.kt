package com.example.weatherapp.utils

import androidx.annotation.DrawableRes
import com.example.weatherapp.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@DrawableRes
fun getWeatherIcon(icon: String): Int {
    val c = icon.lowercase()

    return when {
        c.contains("sun") || c.contains("clear") -> R.drawable.sunny
        c.contains("cloud") && c.contains("part") && c.contains("night") -> R.drawable.partialy_cloudy
        c.contains("cloud") && c.contains("part") -> R.drawable.partialy_cloudy
        c.contains("cloud") -> R.drawable.cloudy
        c.contains("rain") && c.contains("snow") -> R.drawable.sonwy_rainy
        c.contains("rain") -> R.drawable.rainy
        c.contains("snow") -> R.drawable.snowy
        c.contains("fog") || c.contains("mist") -> R.drawable.foggy
        c.contains("wind") -> R.drawable.windy
        c.contains("thunder") || c.contains("lightning") -> R.drawable.lightning
        c.contains("clear") && c.contains("day") -> R.drawable.sunny
        c.contains("clear") && c.contains("night") -> R.drawable.moon
        else -> R.drawable.cloudy
    }
}

fun formatDate(dateStr: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // API’den gelen tarih formatı
        val outputFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale("en"))
        val date = LocalDate.parse(dateStr, inputFormatter)
        date.format(outputFormatter)
    } catch (e: Exception) {
        dateStr // Formatlanamazsa ham stringi döndür
    }
}