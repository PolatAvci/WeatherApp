package com.example.weatherapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.weatherapp.utils.getWeatherIcon

@Composable
fun WeatherCard(day: DayWeather) {

    val gradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF90CAF9),
            Color(0xFF42A5F5)
        )
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🌤 ICON
                Image(
                    painter = painterResource(id = getWeatherIcon(day.conditions)),
                    contentDescription = day.conditions,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                // 📅 INFO
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = day.datetime,
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        TempChip(
                            label = "MAX",
                            value = "${day.tempmax}°",
                            color = Color(0xFFFFCDD2)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TempChip(
                            label = "MIN",
                            value = "${day.tempmin}°",
                            color = Color(0xFFBBDEFB)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TempChip(label: String, value: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(50),
        color = color
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label ",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Text(
                text = value,
                fontSize = 12.sp
            )
        }
    }
}
