package com.example.weatherapp.model

data class DayWeather(
    val datetime: String,
    val temp: Double,           // ortalama sıcaklık
    val tempmax: Double,        // maksimum sıcaklık
    val tempmin: Double,        // minimum sıcaklık
    val feelslike: Double,      // hissedilen sıcaklık
    val humidity: Double,       // nem yüzdesi
    val windspeed: Double,      // rüzgar hızı
    val sunrise: String,        // güneş doğuşu
    val sunset: String,         // güneş batışı
    val conditions: String,     // hava durumu açıklaması
    val icon: String            // ikon adı
)
