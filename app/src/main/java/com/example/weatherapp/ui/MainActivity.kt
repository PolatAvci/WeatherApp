package com.example.weatherapp.ui
import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.composable
import com.example.weatherapp.utils.LocationPrefs
import java.util.Locale

// MainActivity.kt içeriği
// ... (imports ve class tanımı)
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repository = WeatherRepository(RetrofitClient.api)
            val factory = WeatherViewModelFactory(repository)
            val weatherViewModel: WeatherViewModel = viewModel(factory = factory)

            // 1️⃣ Kaydedilmiş konumu al
            val savedLocation = LocationPrefs.getSavedLocation(this)
            var cityName by remember { mutableStateOf(savedLocation.first ?: "Istanbul") }
            var townName by remember { mutableStateOf(savedLocation.second ?: "") }

            val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
            val navController = androidx.navigation.compose.rememberNavController()

            // 2️⃣ Eğer kayıtlı konum yoksa mevcut konumu al
            LaunchedEffect(locationPermissionState.status.isGranted) {
                if (savedLocation.first == null) { // kayıtlı şehir yoksa
                    if (!locationPermissionState.status.isGranted) {
                        locationPermissionState.launchPermissionRequest()
                    } else {
                        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            location?.let {
                                val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                                cityName = addresses?.firstOrNull()?.adminArea ?: cityName
                                townName = addresses?.firstOrNull()?.subAdminArea ?: townName

                                // 3️⃣ Konumu kaydet
                                LocationPrefs.saveLocation(this@MainActivity, cityName, townName)

                                weatherViewModel.loadWeather(
                                    location = cityName,
                                    apiKey = BuildConfig.VISUAL_CROSSING_API_KEY
                                )
                            } ?: run {
                                weatherViewModel.loadWeather(
                                    location = cityName,
                                    apiKey = BuildConfig.VISUAL_CROSSING_API_KEY
                                )
                            }
                        }
                    }
                } else {
                    // Kayıtlı konum varsa onu kullan
                    weatherViewModel.loadWeather(
                        location = if (townName.isNotBlank()) "$cityName, $townName" else cityName,
                        apiKey = BuildConfig.VISUAL_CROSSING_API_KEY
                    )
                }
            }

            WeatherAppTheme {
                androidx.navigation.compose.NavHost(
                    navController = navController,
                    startDestination = "weather_screen"
                ) {
                    composable("weather_screen") {
                        WeatherScreen(
                            weatherViewModel,
                            cityName,
                            navController
                        )
                    }
                    composable("location_change_screen/{cityName}") { backStackEntry ->
                        val currentCity = backStackEntry.arguments?.getString("cityName") ?: cityName
                        val context = LocalContext.current
                        LocationChangeScreen(
                            cityName = currentCity,
                            onCitySelected = { newCity ->
                                cityName = newCity
                                townName = ""
                                // Yeni seçilen konumu kaydet
                                LocationPrefs.saveLocation(context, cityName, townName)
                                weatherViewModel.loadWeather(
                                    location = cityName,
                                    apiKey = BuildConfig.VISUAL_CROSSING_API_KEY
                                )
                            },
                            onBack = { navController.popBackStack() },
                            context = context // buraya ekledik
                        )
                    }
                }
            }
        }
    }
}
