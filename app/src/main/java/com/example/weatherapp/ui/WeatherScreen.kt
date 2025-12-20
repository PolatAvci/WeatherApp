package com.example.weatherapp.ui
import TodayWeatherCard
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.components.WeatherCard
import com.example.weatherapp.viewmodel.WeatherViewModel
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.navigation.NavController
import com.example.weatherapp.BuildConfig
import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.weatherapp.R
import java.util.*


@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    cityName: String,
    navController: NavController
) {
    val weatherData by viewModel.weatherData.observeAsState()
    val context = LocalContext.current

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        val dialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val monthFormatted = (month + 1).toString().padStart(2, '0')
                val dayFormatted = dayOfMonth.toString().padStart(2, '0')
                onDateSelected("$year-$monthFormatted-$dayFormatted")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    // 🔥 ROOT CONTAINER
    Box(modifier = Modifier.fillMaxSize()) {

        // ---- BACKGROUND IMAGE ----
        Image(
            painter = painterResource(id = R.drawable.bg2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // ---- OPTIONAL DARK OVERLAY (ÖNERİLİR) ----
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.0f))
        )

        // ---- CONTENT ----
        Column(modifier = Modifier.fillMaxSize()) {

            // Şehir başlığı
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clickable {
                        navController.navigate("location_change_screen/$cityName")
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = cityName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            // Tarih filtreleri
            DateFilterBar(
                startDate = startDate,
                endDate = endDate,
                onStartClick = { showDatePicker { startDate = it } },
                onEndClick = { showDatePicker { endDate = it } },
                onFilterClick = {
                    if (startDate.isNotBlank() && endDate.isNotBlank()) {
                        viewModel.loadWeather(
                            location = cityName.split(",")[0],
                            startDate = startDate,
                            endDate = endDate,
                            apiKey = BuildConfig.VISUAL_CROSSING_API_KEY
                        )
                    }
                }
            )

            // Weather cards
            when (weatherData) {
                null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
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
                            item { TodayWeatherCard(day = days[0]) }
                            items(days.drop(1)) { day ->
                                WeatherCard(day)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DateFilterBar(
    startDate: String,
    endDate: String,
    onStartClick: () -> Unit,
    onEndClick: () -> Unit,
    onFilterClick: () -> Unit
) {

    val backgroundColor = Color(0xFF357ABD)
    val borderColor = Color.White.copy(alpha = 0.9f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            DateChip(
                label = "Başlangıç",
                value = startDate,
                onClick = onStartClick,
                modifier = Modifier.weight(1f)
            )

            DateChip(
                label = "Bitiş",
                value = endDate,
                onClick = onEndClick,
                modifier = Modifier.weight(1f)
            )

            FilterButton(
                enabled = startDate.isNotBlank() && endDate.isNotBlank(),
                onClick = onFilterClick
            )
        }
    }
}

@Composable
fun DateChip(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF4A90E2).copy(alpha = 0.25f), // daha canlı mavi
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = label.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.8f),
                letterSpacing = 1.sp
            )
            Text(
                text = if (value.isBlank()) "Tarih seç" else value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun FilterButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(48.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (enabled)
            Color(0xFF4A90E2) // canlı turkuaz
        else
            Color(0xFF4FC3F7).copy(alpha = 0.4f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Search,
                contentDescription = "Filtrele",
                tint = Color.White
            )
        }
    }
}

