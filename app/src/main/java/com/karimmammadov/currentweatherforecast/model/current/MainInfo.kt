package com.karimmammadov.currentweatherforecast.model.current

import com.google.gson.annotations.SerializedName

data class MainInfo(
    @SerializedName("temp")
    val temperature: Double
)