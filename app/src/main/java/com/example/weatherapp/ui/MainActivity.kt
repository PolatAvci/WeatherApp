package com.example.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.viewmodel.WeatherViewModelFactory
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.api.RetrofitClient
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import android.location.Geocoder
import java.util.Locale

// MainActivity.kt içeriği
// ... (imports ve class tanımı)

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ... (repository, factory, viewModel tanımları)
            val repository = WeatherRepository(RetrofitClient.api)
            val factory = WeatherViewModelFactory(repository)
            val weatherViewModel: WeatherViewModel = viewModel(factory = factory)

            // 1. Şehir ismini (durumu) izlemek için, bu şehir ismini WeatherScreen'e geçirin
            var cityName by remember { mutableStateOf("Istanbul") } // Varsayılan/Geri dönüş değeri
            var townName by remember { mutableStateOf("") } // Varsayılan/Geri dönüş değeri

            val locationPermissionState = rememberPermissionState(
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            // AŞAMA 1: LaunchedEffect'i izin durumu değiştiğinde çalışacak şekilde güncelleyin
            LaunchedEffect(locationPermissionState.status.isGranted) {
                if (!locationPermissionState.status.isGranted) {
                    // İzin yoksa iste
                    locationPermissionState.launchPermissionRequest()
                } else {
                    // İzin varsa konumu al ve yükle
                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                            val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)

                            // 2. cityName durumunu güncelle
                            cityName = addresses?.firstOrNull()?.adminArea ?: "Istanbul"
                            townName = addresses?.firstOrNull()?.subAdminArea ?: "Istanbul"

                            // 3. Hava durumu verisini yükle
                            weatherViewModel.loadWeather(
                                location = cityName,
                                apiKey = BuildConfig.VISUAL_CROSSING_API_KEY
                            )
                        } ?: run {
                            // Konum alınamazsa varsayılan şehri yükle
                            weatherViewModel.loadWeather(
                                location = cityName,
                                apiKey = BuildConfig.VISUAL_CROSSING_API_KEY
                            )
                        }
                    }
                }
            }

            WeatherScreen(weatherViewModel, cityName + ", " + townName)
        }
    }
}

