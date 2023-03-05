package com.karimmammadov.currentweatherforecast.model.current

import com.google.gson.annotations.SerializedName


data class WeatherInfo(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
)