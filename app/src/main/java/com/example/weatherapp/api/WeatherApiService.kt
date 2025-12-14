package com.example.weatherapp.api

import com.example.weatherapp.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiService {

    /**
     * location: şehir adı (örn: "London,UK")
     * startDate: YYYY-MM-DD formatında isteğe bağlı
     * endDate: YYYY-MM-DD formatında isteğe bağlı
     */
    @GET("VisualCrossingWebServices/rest/services/timeline/{location}/{startDate}/{endDate}")
    suspend fun getWeatherWithDates(
        @Path("location") location: String,
        @Path("startDate") startDate: String,
        @Path("endDate") endDate: String,
        @Query("unitGroup") unitGroup: String = "metric",
        @Query("key") apiKey: String
    ): WeatherResponse

    /**
     * Tarih filtrelemeden mevcut çağrı
     */
    @GET("VisualCrossingWebServices/rest/services/timeline/{location}")
    suspend fun getWeather(
        @Path("location") location: String,
        @Query("unitGroup") unitGroup: String = "metric",
        @Query("key") apiKey: String
    ): WeatherResponse
}
