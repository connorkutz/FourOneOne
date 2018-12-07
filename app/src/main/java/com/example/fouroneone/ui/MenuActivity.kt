/*
* Placeholder activity to be replaced with a fancier design.
* */

package com.example.fouroneone.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.fouroneone.R
import com.example.fouroneone.managers.FortuneCookieManager
import com.example.fouroneone.managers.WeatherManager

class MenuActivity : AppCompatActivity() {

    private lateinit var fortuneCookieButton: Button
    private lateinit var spotifyButton: Button
    private lateinit var weatherButton: Button
    private lateinit var weatherTemperatureText: TextView
    private lateinit var weatherDescriptionText: TextView
    private lateinit var fortuneCookieQuote: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        fortuneCookieButton = findViewById(R.id.fortunecookie_button)
        spotifyButton = findViewById(R.id.spotify_button)
        weatherButton = findViewById(R.id.menu_weather_button)
        weatherTemperatureText = findViewById(R.id.menu_weather_temperature)
        weatherDescriptionText = findViewById(R.id.menu_weather_description)
        fortuneCookieQuote = findViewById(R.id.menu_fortune_cookie_text)

        WeatherManager().getCurrentWeather(
                apiKey = getString(R.string.weather_key),
                successCallback = {weather ->
                    runOnUiThread{
                        weatherTemperatureText.text = weather.temperature.toString()
                        weatherDescriptionText.text = weather.description
                    }

                },
                errorCallback = {exception ->
                    Log.d("MenuActivity", exception.message)
                }
        )

        FortuneCookieManager().getQuote(
                successCallback = {quote ->
                    runOnUiThread {
                        fortuneCookieQuote.text = quote
                    }
                },
                errorCallback = {exception ->
                    Log.d("MenuActivity", exception.message)
                }
        )

        fortuneCookieButton.setOnClickListener {
            val intent = Intent(this, FortuneCookieActivity::class.java)
            startActivity(intent)
        }
        spotifyButton.setOnClickListener {
            val intent = Intent(this, SpotifyActivity::class.java)
            startActivity(intent)
        }
        weatherButton.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
        }
    }
}
