package com.example.weatherapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.components.WeatherCard
import com.example.weatherapp.viewmodel.WeatherViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.weatherapp.model.DayWeather
import com.example.weatherapp.utils.formatDate
import com.example.weatherapp.utils.getWeatherIcon

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weatherData by viewModel.weatherData.observeAsState()

    when (weatherData) {
        null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        else -> {
            val days = weatherData!!.days
            if (days.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Bugünün hava durumu
                    item {
                        TodayWeatherCard(day = days[0])
                    }

                    // Diğer günler
                    items(days.drop(1)) { day ->
                        WeatherCard(day)
                    }
                }
            }
        }
    }
}

@Composable
fun TodayWeatherCard(day: DayWeather) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4FC3F7), Color(0xFF0288D1))
    )

    val dateFormatted = formatDate(day.datetime)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Tarih ve ana durum
                Text(
                    text = "Today - $dateFormatted",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = getWeatherIcon(day.icon)),
                    contentDescription = day.conditions,
                    modifier = Modifier
                                .size(96.dp)
                                .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = day.conditions,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Sıcaklık bilgileri: LazyRow ile taşmaları önlüyoruz
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item { InfoChip("Temp", "${day.temp}°") }
                    item { InfoChip("Max", "${day.tempmax}°") }
                    item { InfoChip("Min", "${day.tempmin}°") }
                    item { InfoChip("Feels", "${day.feelslike}°") }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item { InfoChip("Humidity", "${day.humidity}%") }
                    item { InfoChip("Wind", "${day.windspeed} km/h") }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item { InfoChip("Sunrise", day.sunrise) }
                    item { InfoChip("Sunset", day.sunset) }
                }
            }
        }
    }
}

@Composable
fun InfoChip(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.White.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label: ",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color.White
            )
            Text(
                text = value,
                fontSize = 13.sp,
                color = Color.White
            )
        }
    }
}
