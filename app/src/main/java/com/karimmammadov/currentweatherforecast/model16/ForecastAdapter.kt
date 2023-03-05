package com.example.hourweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karimmammadov.currentweatherforecast.R
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(private val weatherList: List<WeatherData>) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val iconImageView: ImageView = itemView.findViewById(R.id.weatherIconImageView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.weatherDescriptionTextView)
        private val temperatureTextView: TextView = itemView.findViewById(R.id.temperatureTextView)

        fun bind(weather: Weather) {
            // Set the views with the weather data
            val date = Date(weather.date * 1000L)
            val dateFormatter = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
            dateTextView.text = dateFormatter.format(date)
            descriptionTextView.text = weather.description
            temperatureTextView.text = String.format("%.1f ℃", weather.temperature)
            val iconUrl = "http://openweathermap.org/img/w/${weather.icon}.png"
            Glide.with(iconImageView.context)
                .load(iconUrl)
                .into(iconImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_weather_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather = weatherList[position].weatherList.first()
        holder.bind(weather)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }
}