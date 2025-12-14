package com.example.weatherapp.viewmodel

import androidx.lifecycle.*
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch


class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData

    /**
     * location: şehir adı
     * startDate, endDate: YYYY-MM-DD formatında isteğe bağlı tarih filtresi
     */
    fun loadWeather(location: String, apiKey: String, startDate: String? = null, endDate: String? = null) {
        viewModelScope.launch {
            try {
                _weatherData.value = repository.fetchWeather(
                    location = location,
                    apiKey = apiKey,
                    startDate = startDate,
                    endDate = endDate
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
