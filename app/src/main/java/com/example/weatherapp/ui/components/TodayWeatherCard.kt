import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.model.DayWeather
import com.example.weatherapp.ui.components.ModernTempDisplay
import com.example.weatherapp.utils.formatDate
import com.example.weatherapp.utils.getWeatherCondition
import com.example.weatherapp.utils.getWeatherIcon

@Composable
fun TodayWeatherCard(day: DayWeather) {

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4A90E2), Color(0xFF357ABD))
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(32.dp)),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .background(gradient)
                .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(32.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ---- HEADER ----
            Text(
                text = "Bugün",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 1.sp
            )

            Text(
                text = formatDate(day.datetime),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ---- ICON ----
            Image(
                painter = painterResource(id = getWeatherIcon(day.icon)),
                contentDescription = null,
                modifier = Modifier.size(96.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---- CONDITION ----
            Text(
                text = getWeatherCondition(day.conditions),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---- MAIN TEMP ----
            Text(
                text = "${day.temp.toInt()}°",
                fontSize = 64.sp,
                fontWeight = FontWeight.Thin,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---- MAX / MIN ----
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ModernTempDisplay("Max", "${day.tempmax.toInt()}°", Color(0xFFFF8A80))
                ModernTempDisplay("Min", "${day.tempmin.toInt()}°", Color(0xFF81D4FA))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ---- INFO CHIPS ----
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { TodayInfoItem("Hissedilen", "${day.feelslike}°") }
                item { TodayInfoItem("Nem", "%${day.humidity}") }
                item { TodayInfoItem("Rüzgar", "${day.windspeed} km/h") }
                item { TodayInfoItem("Gün Doğumu", day.sunrise) }
                item { TodayInfoItem("Gün Batımı", day.sunset) }
            }
        }
    }
}

@Composable
fun TodayInfoItem(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
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
