package com.karimmammadov.currentweatherforecast.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karimmammadov.currentweatherforecast.R
import com.karimmammadov.currentweatherforecast.databinding.ActivityMainBinding
import com.karimmammadov.currentweatherforecast.getLotties.GetWeatherLotties
import com.karimmammadov.currentweatherforecast.model.current.WeatherModel
import com.karimmammadov.currentweatherforecast.service.RetrofitService
import com.karimmammadov.currentweatherforecast.service.WeatherAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private val LOCATION_REQUEST_CODE = 101
    private lateinit var openWeatherMapApi: WeatherAPI

    private val apiKey = "16b2e30a5f1379b1d8eea7014dc75149"
    private lateinit var compositeDisposable: CompositeDisposable
    var BASE_URL = "https://api.openweathermap.org/data/2.5/"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        binding.citySearch.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                getCityWeather(binding.citySearch.text.toString())
                val view = this.currentFocus
                if (view != null) {
                    val im: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    im.hideSoftInputFromWindow(view.windowToken, 0)
                    binding.citySearch.clearFocus()
                }
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
        binding.currentLocation.setOnClickListener {
            getCurrentLocation()
        }

    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCityWeather(city: String) {
        binding.progressBar.visibility = View.VISIBLE

        compositeDisposable = CompositeDisposable()
        val retrofit = RetrofitService(BASE_URL).retrofit.create(WeatherAPI::class.java)
        compositeDisposable.add(retrofit.getCityWeatherData(city, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.progressBar.visibility = View.GONE
                setData(it)
            }, { throwable ->
                Toast.makeText(this@MainActivity, "City not found", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            })
        )

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchCurrentLocationWeather(latitude: String, longtitude: String) {

        compositeDisposable = CompositeDisposable()
        val retrofit = RetrofitService(BASE_URL).retrofit.create(WeatherAPI::class.java)
        compositeDisposable.add(retrofit.getCurrentWeatherData(latitude, longtitude, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.progressBar.visibility = View.GONE
                setData(it)
            }, { throwable ->
                Toast.makeText(this@MainActivity, throwable.message, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            })
        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProvider.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            currentLocation = location
                            binding.progressBar.visibility = View.VISIBLE
                            fetchCurrentLocationWeather(
                                location.latitude.toString(),
                                location.longitude.toString()
                            )
                        }
                    }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_REQUEST_CODE
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {

            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData(body: WeatherModel) {
        binding.apply {

            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentTime = sdf.format(Date())
            println("Current time: $currentTime")

            val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdfDate.format(Date())
            println("Current date: $currentDate")

            dateTime.text = currentDate + " " + currentTime
            cityName.text = body.sys.country + "-" + body.name
            maxTemp.text = "Max: " + minmax(body?.main?.temp_max!!) + "°"
            minTemp.text = "Min: " + minmax(body?.main?.temp_min!!) + "°"
            temp.text = "" + minmax(body?.main?.temp!!) + "°"
            weatherTitle.text = body.weather[0].main
            sunriseValue.text = correctTime(body.sys.sunrise.toLong())
            binding.sunriseImg.playAnimation()
            sunsetValue.text = correctTime(body.sys.sunset.toLong())
            binding.sunsetImg.playAnimation()
            humidityValue.text = body.main.humidity.toString() + "%"
            humidityImg.playAnimation()
            tempFValue.text = "" + (minmax(body.main.temp).times(1.8)).plus(32).roundToInt()
            tempImg.playAnimation()
            citySearch.setText(body.name)
            feelsLike.text = "Feels Like: " + minmax(body.main.feels_like) + "°"
            windValue.text = body.wind.speed.toString() + "m/s"
            windImg.playAnimation()
        }
        updateUI(body.weather[0].id)
    }

    private fun updateUI(id: Int) {
        binding.apply {
            when (id) {
                //Thunderstorm
                in 200..232 -> {
                    val getWeatherLotties = GetWeatherLotties().getThunderstormLottie()
                    weatherImg.setAnimation(getWeatherLotties)
                    binding.weatherImg.playAnimation()
                    val currentBackground = GetWeatherLotties().getCurrentBackground()
                    mainLayout.setBackgroundResource(currentBackground)
                    optionsLayout.setBackgroundResource(currentBackground)
                    optionsLayout2.setBackgroundResource(currentBackground)
                }

                //Drizzle
                in 300..321 -> {
                    val getWeatherLotties = GetWeatherLotties().getDrizzleLottie()
                    weatherImg.setAnimation(getWeatherLotties)
                    binding.weatherImg.playAnimation()
                    val currentBackground = GetWeatherLotties().getCurrentBackground()
                    mainLayout.setBackgroundResource(currentBackground)
                    optionsLayout.setBackgroundResource(currentBackground)
                    optionsLayout2.setBackgroundResource(currentBackground)
                }

                //Rain
                in 500..531 -> {
                    val getWeatherLotties = GetWeatherLotties().getRainyLottie()
                    weatherImg.setAnimation(getWeatherLotties)
                    binding.weatherImg.playAnimation()
                    val currentBackground = GetWeatherLotties().getCurrentBackground()
                    mainLayout.setBackgroundResource(currentBackground)
                    optionsLayout.setBackgroundResource(currentBackground)
                    optionsLayout2.setBackgroundResource(currentBackground)
                }

                //Snow
                in 600..622 -> {
                    val getWeatherLotties = GetWeatherLotties().getSnowLottie()
                    weatherImg.setAnimation(getWeatherLotties)
                    binding.weatherImg.playAnimation()
                    val currentBackground = GetWeatherLotties().getCurrentBackground()
                    mainLayout.setBackgroundResource(currentBackground)
                    optionsLayout.setBackgroundResource(currentBackground)
                    optionsLayout2.setBackgroundResource(currentBackground)
                }

                //Atmosphere
                in 701..781 -> {
                    val getWeatherLotties = GetWeatherLotties().getAFewClouds()
                    weatherImg.setAnimation(getWeatherLotties)
                    binding.weatherImg.playAnimation()
                    val currentBackground = GetWeatherLotties().getCurrentBackground()
                    mainLayout.setBackgroundResource(currentBackground)
                    optionsLayout.setBackgroundResource(currentBackground)
                    optionsLayout2.setBackgroundResource(currentBackground)
                }

                //Clear
                800 -> {
                    val getWeatherLotties = GetWeatherLotties().getClearWeather()
                    weatherImg.setAnimation(getWeatherLotties)
                    binding.weatherImg.playAnimation()
                    val currentBackground = GetWeatherLotties().getCurrentBackground()
                    mainLayout.setBackgroundResource(currentBackground)
                    optionsLayout.setBackgroundResource(currentBackground)
                    optionsLayout2.setBackgroundResource(currentBackground)
                }

                //Clouds
                in 801..804 -> {
                    val getWeatherLotties = GetWeatherLotties().getCloudyWeather()
                    weatherImg.setAnimation(getWeatherLotties)
                    binding.weatherImg.playAnimation()
                    val currentBackground = GetWeatherLotties().getCurrentBackground()
                    mainLayout.setBackgroundResource(currentBackground)
                    optionsLayout.setBackgroundResource(currentBackground)
                    optionsLayout2.setBackgroundResource(currentBackground)
                }

                //unknown
                else -> {
                    weatherImg.setAnimation(R.raw.unknown)
                    binding.weatherImg.playAnimation()
                    mainLayout.background =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.unknown_bg)
                    optionsLayout.background =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.unknown_bg)
                    optionsLayout2.background =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.unknown_bg)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun correctTime(time: Long): String {
        val localTime = time.let {
            Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalTime()
        }
        return localTime.toString()
    }

    private fun minmax(tempMax: Double): Double {
        var intTemp = tempMax
        intTemp = intTemp.minus(273)
        return intTemp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
    }

}