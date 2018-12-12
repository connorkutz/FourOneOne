package com.example.fouroneone

import android.graphics.drawable.AnimationDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

class WeatherActivity : AppCompatActivity() {

    private lateinit var tempText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var titleText: TextView
    private lateinit var humidityText: TextView
    private lateinit var windSpeedText: TextView
    private lateinit var feelsLikeText: TextView
    private lateinit var cloudsText: TextView
    private lateinit var animation: AnimationDrawable
    private lateinit var icon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val container = findViewById<ConstraintLayout>(R.id.container)
        animation = container.background as AnimationDrawable
        animation.setEnterFadeDuration(4000)
        animation.setExitFadeDuration(8000)

        tempText = findViewById(R.id.weather_temperature_text)
        descriptionText = findViewById(R.id.weather_description_text)
        titleText = findViewById(R.id.weather_title)
        humidityText = findViewById(R.id.weather_humidity_text)
        windSpeedText = findViewById(R.id.weather_windspeed_text)
        feelsLikeText = findViewById(R.id.weather_feelslike_text)
        cloudsText = findViewById(R.id.weather_clouds_text)
        icon = findViewById(R.id.weather_icon)

        val key = getString(R.string.weather_key)
        WeatherManager().getCurrentWeather(
                apiKey = key,
                successCallback = { currentWeather ->
                    val resID = resources.getIdentifier(currentWeather.iconCode, "drawable", packageName)
                    runOnUiThread{
                        tempText.text = currentWeather.temperature
                        descriptionText.text = currentWeather.description
                        titleText.append(currentWeather.cityName)
                        humidityText.append(currentWeather.pressure)
                        windSpeedText.append(currentWeather.windSpeed)
                        feelsLikeText.append(currentWeather.feelsLike)
                        cloudsText.append(currentWeather.clouds)
                        icon.setImageResource(resID)
                    }

                },
                errorCallback = { e ->
                    Log.d("Weather", e.message)
                }
        )


    }

    override fun onPause() {
        super.onPause()
        if(animation.isRunning){
            animation.stop()
        }
    }
}