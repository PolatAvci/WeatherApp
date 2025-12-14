import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationChangeScreen(
    cityName: String,
    onCitySelected: (String) -> Unit,
    onBack: () -> Unit
) {
    var newCity by remember { mutableStateOf("") }
    val oldCity = remember { cityName }

    val cities = listOf(
        "Istanbul", "Ankara", "Izmir", "Bursa", "Antalya",
        "Adana", "Konya", "Gaziantep", "Mersin", "Kayseri",
        "Trabzon", "Samsun", "Eskişehir", "Denizli"
    )

    val filteredCities = remember(newCity) {
        cities.filter {
            it.contains(newCity, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        /* 🔹 TOP BAR */
        TopAppBar(
            title = {
                Text(
                    text = "Şehir değiştir",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onBack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Geri"
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            TextField(
                value = newCity,
                onValueChange = { newCity = it },
                label = { Text("Şehir ara") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {
                items(filteredCities) { city ->
                    Text(
                        text = city,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // ✅ Yeni şehir seçildi
                                onCitySelected(city)
                                onBack()
                            }
                            .padding(vertical = 12.dp)
                    )
                    Divider()
                }
            }
        }
    }
}

