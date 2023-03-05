package com.example.hourweather

import com.google.gson.annotations.SerializedName

data class WeatherData(
    val city: String,
    val temperature: String,
    val weatherList: List<Weather>
)
