package com.example.weatherapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.model.DayWeather
import com.example.weatherapp.utils.formatDate
import com.example.weatherapp.utils.getWeatherCondition
import com.example.weatherapp.utils.getWeatherIcon

@Composable
fun WeatherCard(day: DayWeather) {
    var expanded by remember { mutableStateOf(false) }

    // Modern Derin Gradyan (Gece/Gündüz moduna göre güncellenebilir)
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4A90E2), Color(0xFF357ABD))
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(28.dp))
            .clickable { expanded = !expanded }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Modern tasarımda gölge yerine border tercih edilir
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .background(backgroundGradient)
                .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(28.dp))
                .padding(20.dp)
        ) {
            // Üst Bölüm: Tarih ve Ana Durum
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = formatDate(day.datetime),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = getWeatherCondition(day.conditions),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                // Derece Göstergesi
                Text(
                    text = "${day.temp.toInt()}°",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Thin,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Orta Bölüm: İkon ve Max/Min
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = getWeatherIcon(day.icon)),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(20.dp))

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    ModernTempDisplay("Max", "${day.tempmax.toInt()}°", Color(0xFFFF8A80))
                    Spacer(modifier = Modifier.width(16.dp))
                    ModernTempDisplay("Min", "${day.tempmin.toInt()}°", Color(0xFF81D4FA))
                }
            }

            // Genişleyen Detay Bölümü
            AnimatedVisibility(visible = expanded) {
                Column {
                    Divider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color.White.copy(alpha = 0.1f)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 4.dp)
                    ) {
                        item { DetailedInfoItem("Hissedilen", "${day.feelslike}°") }
                        item { DetailedInfoItem("Nem", "%${day.humidity}") }
                        item { DetailedInfoItem("Rüzgar", "${day.windspeed} km/h") }
                        item { DetailedInfoItem("Gün Doğumu", day.sunrise) }
                        item { DetailedInfoItem("Gün Batımı", day.sunset) }
                    }
                }
            }
        }
    }
}

@Composable
fun ModernTempDisplay(label: String, value: String, accentColor: Color) {
    Column {
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.6f),
            letterSpacing = 1.sp
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .width(20.dp)
                .height(3.dp)
                .background(accentColor, RoundedCornerShape(2.dp))
        )
    }
}

@Composable
fun DetailedInfoItem(label: String, value: String) {
    Surface(
        color = Color.White.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        // border = border(...) yerine BorderStroke kullanıyoruz:
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}