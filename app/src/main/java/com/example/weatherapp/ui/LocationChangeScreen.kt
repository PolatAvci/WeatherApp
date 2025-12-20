package com.example.weatherapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.weatherapp.utils.LocationPrefs
import com.google.android.gms.location.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationChangeScreen(
    cityName: String,
    onCitySelected: (String) -> Unit,
    onBack: () -> Unit,
    context: Context
) {
    var searchQuery by remember { mutableStateOf("") }

    val cities = listOf(
        "Konumunuz",
        "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Ankara", "Antalya",
        "Artvin", "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur",
        "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne",
        "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane",
        "Hakkari", "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir", "Kars", "Kastamonu",
        "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya",
        "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu",
        "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat",
        "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray",
        "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır",
        "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"
    ).sortedBy { if (it == "Konumunuz") "" else it }

    val filteredCities = remember(searchQuery) {
        cities.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    var currentLocationCity by remember { mutableStateOf(cityName) }

    // İzin launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                // izin verildiyse konumu al
                getCurrentCityFromDevice(context) { city ->
                    currentLocationCity = city
                    LocationPrefs.saveLocation(context, city, "")
                    onCitySelected(city)
                    onBack()
                }
            }
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                title = {
                    Text(
                        "Konum Seç",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Şu anki konumunuz",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            currentLocationCity,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                placeholder = { Text("Şehir ara...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredCities) { city ->
                    CityItem(
                        cityName = city,
                        isSelected = city == currentLocationCity,
                        onClick = {
                            if (city == "Konumunuz") {
                                // izin kontrolü
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            } else {
                                LocationPrefs.saveLocation(context, city, "")
                                currentLocationCity = city
                                onCitySelected(city)
                                onBack()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CityItem(cityName: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = cityName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// Konum alma fonksiyonu
fun getCurrentCityFromDevice(context: Context, onCityFound: (String) -> Unit) {
    // İzin kontrolü
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
    ) {
        onCityFound("Istanbul")
        return
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            try {
                val geocoder = Geocoder(context)
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val city = addresses?.firstOrNull()?.adminArea ?: "Istanbul"
                val townName = addresses?.firstOrNull()?.subAdminArea ?: "Merkez"
                onCityFound(city)
            } catch (e: Exception) {
                e.printStackTrace()
                onCityFound("Istanbul")
            }
        } else {
            // location null ise fallback
            onCityFound("Istanbul")
        }
    }.addOnFailureListener {
        it.printStackTrace()
        onCityFound("Istanbul")
    }
}
