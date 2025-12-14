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
import androidx.compose.ui.platform.LocalContext
import java.util.*



@Composable
fun WeatherScreen(viewModel: WeatherViewModel, cityName: String, navController: NavController) {
    val weatherData by viewModel.weatherData.observeAsState()
    val context = LocalContext.current

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    // DatePicker dialogları
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
                imageVector = androidx.compose.material.icons.Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = cityName,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }

        // Tarih filtreleri
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(onClick = { showDatePicker { startDate = it } }) {
                Text(if (startDate.isBlank()) "Başlangıç tarihi" else startDate)
            }
            OutlinedButton(onClick = { showDatePicker { endDate = it } }) {
                Text(if (endDate.isBlank()) "Bitiş tarihi" else endDate)
            }
            Button(onClick = {
                if (startDate.isNotBlank() && endDate.isNotBlank()) {
                    viewModel.loadWeather(
                        location = cityName.split(",")[0],
                        startDate = startDate,
                        endDate = endDate,
                        apiKey = BuildConfig.VISUAL_CROSSING_API_KEY
                    )
                }
            }) {
                Text("Filtrele")
            }
        }

        // Weather Card
        when (weatherData) {
            null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
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
                        items(days.drop(1)) { day -> WeatherCard(day) }
                    }
                }
            }
        }
    }
}
