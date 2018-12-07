package com.example.fouroneone.ui

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.fouroneone.R
import com.example.fouroneone.managers.WeatherManager

class WeatherActivity : AppCompatActivity() {

    private lateinit var weatherText: TextView
    private lateinit var weatherButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        weatherText = findViewById(R.id.weather_text)
        weatherButton = findViewById(R.id.weather_button)

        weatherButton.setOnClickListener {
            val key = getString(R.string.weather_key)
            WeatherManager().getCurrentWeather(
                    apiKey = key,
                    successCallback = {currentWeather ->
                        //TODO take weather object and display
                    },
                    errorCallback = {e ->
                        Log.d("Weather", e.message)
                    }
            )
        }

    }
}
