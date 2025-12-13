package com.example.weatherapp.ui
import com.example.weatherapp.BuildConfig
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.viewmodel.WeatherViewModelFactory
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.api.RetrofitClient
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val repository = WeatherRepository(RetrofitClient.api)
                    val factory = WeatherViewModelFactory(repository)
                    val weatherViewModel: WeatherViewModel = viewModel(factory = factory)

                    // Istanbul için örnek yükleme
                    LaunchedEffect(Unit) {
                        weatherViewModel.loadWeather(
                            location = "Istanbul",
                            apiKey = BuildConfig.VISUAL_CROSSING_API_KEY
                        )
                    }

                    WeatherScreen(weatherViewModel)
                }
            }
        }
    }
}
