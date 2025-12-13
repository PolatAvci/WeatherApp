package com.example.weatherapp.viewmodel

import androidx.lifecycle.*
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData

    fun loadWeather(location: String, apiKey: String) {
        viewModelScope.launch {
            try {
                _weatherData.value = repository.fetchWeather(location, apiKey)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
