package com.example.fouroneone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class WeatherActivity : AppCompatActivity() {

    private lateinit var tempText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var titleText: TextView
    private lateinit var humidityText: TextView
    private lateinit var windSpeedText: TextView
    private lateinit var feelsLikeText: TextView
    private lateinit var cloudsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        tempText = findViewById(R.id.weather_temperature_text)
        descriptionText = findViewById(R.id.weather_description_text)
        titleText = findViewById(R.id.weather_title)
        humidityText = findViewById(R.id.weather_humidity_text)
        windSpeedText = findViewById(R.id.weather_windspeed_text)
        feelsLikeText = findViewById(R.id.weather_feelslike_text)
        cloudsText = findViewById(R.id.weather_clouds_text)

        val key = getString(R.string.weather_key)
        WeatherManager().getCurrentWeather(
                apiKey = key,
                successCallback = { currentWeather ->
                    runOnUiThread{
                        tempText.text = currentWeather.temperature
                        descriptionText.text = currentWeather.description
                        titleText.append(currentWeather.cityName)
                        humidityText.append(currentWeather.pressure)
                        windSpeedText.append(currentWeather.windSpeed)
                        feelsLikeText.append(currentWeather.feelsLike)
                        cloudsText.append(currentWeather.clouds)
                    }

                },
                errorCallback = { e ->
                    Log.d("Weather", e.message)
                }
        )


    }
}