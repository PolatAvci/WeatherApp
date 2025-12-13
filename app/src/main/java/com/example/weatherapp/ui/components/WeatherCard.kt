package com.example.weatherapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.model.DayWeather
import com.example.weatherapp.utils.formatDate
import com.example.weatherapp.utils.getWeatherIcon

@Composable
fun WeatherCard(day: DayWeather) {
    var expanded by remember { mutableStateOf(false) } // aç/kapa durumu

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF90CAF9), Color(0xFF42A5F5))
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = !expanded }, // tıklanınca aç/kapa
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .background(gradient)
                .padding(16.dp)
        ) {
            // Ana satır
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = getWeatherIcon(day.icon)),
                    contentDescription = day.conditions,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = formatDate(day.datetime),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = day.conditions,
                        fontSize = 15.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Row {
                        TempChip("MAX", "${day.tempmax}°", Color(0xFFFFCDD2))
                        Spacer(modifier = Modifier.width(8.dp))
                        TempChip("MIN", "${day.tempmin}°", Color(0xFFBBDEFB))
                    }
                }
            }

            // Detaylar açıldığında
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item { InfoChip("Temp", "${day.temp}°") }
                    item { InfoChip("Feels", "${day.feelslike}°") }
                    item { InfoChip("Humidity", "${day.humidity}%") }
                    item { InfoChip("Wind", "${day.windspeed} km/h") }
                    item { InfoChip("Sunrise", day.sunrise) }
                    item { InfoChip("Sunset", day.sunset) }
                }
            }
        }
    }
}

@Composable
fun TempChip(label: String, value: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label: ",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color.White
            )
            Text(
                text = value,
                fontSize = 12.sp,
                color = Color.White
            )
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
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
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
