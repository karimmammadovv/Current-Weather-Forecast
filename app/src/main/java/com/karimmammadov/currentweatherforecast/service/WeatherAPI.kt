package com.karimmammadov.currentweatherforecast.service

import com.example.hourweather.WeatherForecast
import com.karimmammadov.currentweatherforecast.model.current.WeatherModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("weather")
    fun getCurrentWeatherData(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("APPID") appid:String
    ): Observable<WeatherModel>

    @GET("weather")
    fun getCityWeatherData(
        @Query("q") q:String,
        @Query("APPID") appid:String
    ):Observable<WeatherModel>

    @GET("forecast/daily")
    fun getWeatherForecast(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("cnt") days: Int,
        @Query("appid") apiKey: String
    ): Observable<WeatherForecast>
}