package com.karimmammadov.currentweatherforecast.getLotties

import com.karimmammadov.currentweatherforecast.R
import java.util.*

class GetWeatherLotties {

    fun getThunderstormLottie(): Int {
        val lottieFileDay = R.raw.daythunderstorm
        val lottieFileNight = R.raw.nightthunderstorm
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return if (hour in 6..17) {
            lottieFileDay // Use day Lottie file from 6:00 to 17:59
        } else {
            lottieFileNight // Use night Lottie file from 18:00 to 5:59
        }
    }

    fun getDrizzleLottie() :Int{
        val lottieFileDay = R.raw.daydrizzle
        val lottieFileNight = R.raw.nightdrizle
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return if (hour in 6..17) {
            lottieFileDay // Use day Lottie file from 6:00 to 17:59
        } else {
            lottieFileNight // Use night Lottie file from 18:00 to 5:59
        }
    }

    fun getRainyLottie() :Int{
        val lottieFileDay = R.raw.dayrain
        val lottieFileNight = R.raw.nightrain
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return if (hour in 6..17) {
            lottieFileDay // Use day Lottie file from 6:00 to 17:59
        } else {
            lottieFileNight // Use night Lottie file from 18:00 to 5:59
        }
    }

    fun getSnowLottie() :Int{
        val lottieFileDay = R.raw.daysnow
        val lottieFileNight = R.raw.nightsnow
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return if (hour in 6..17) {
            lottieFileDay // Use day Lottie file from 6:00 to 17:59
        } else {
            lottieFileNight // Use night Lottie file from 18:00 to 5:59
        }
    }

    fun getAFewClouds() :Int{
        val lottieFileDay = R.raw.dayfewclouds
        val lottieFileNight = R.raw.nightafewclouds
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return if (hour in 6..17) {
            lottieFileDay // Use day Lottie file from 6:00 to 17:59
        } else {
            lottieFileNight // Use night Lottie file from 18:00 to 5:59
        }
    }

    fun getClearWeather() :Int{
        val lottieFileDay = R.raw.dayclear
        val lottieFileNight = R.raw.nightclear
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return if (hour in 6..17) {
            lottieFileDay // Use day Lottie file from 6:00 to 17:59
        } else {
            lottieFileNight // Use night Lottie file from 18:00 to 5:59
        }
    }

    fun getCloudyWeather() :Int{
        val lottieFileDay = R.raw.dayclouds
        val lottieFileNight = R.raw.nightclouds
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return if (hour in 6..17) {
            lottieFileDay // Use day Lottie file from 6:00 to 17:59
        } else {
            lottieFileNight // Use night Lottie file from 18:00 to 5:59
        }
    }

    fun getCurrentBackground(): Int {
        val backgroundDay = R.drawable.rain_bg
        val backgroundNight = R.drawable.snow_bg
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return if (hour in 6..17) {
            backgroundDay // Use day background from 6:00 to 17:59
        } else {
            backgroundNight // Use night background from 18:00 to 5:59
        }
    }
}