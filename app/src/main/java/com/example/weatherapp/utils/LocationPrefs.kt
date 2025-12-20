package com.example.weatherapp.utils
import android.content.Context

object LocationPrefs {

    private const val PREF_NAME = "weather_prefs"
    private const val KEY_CITY = "city"
    private const val KEY_TOWN = "town"

    fun saveLocation(context: Context, city: String, town: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_CITY, city)
            .putString(KEY_TOWN, town)
            .apply()
    }

    fun getSavedLocation(context: Context): Pair<String?, String?> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val city = prefs.getString(KEY_CITY, null)
        val town = prefs.getString(KEY_TOWN, null)
        return Pair(city, town)
    }

    fun clearLocation(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}
