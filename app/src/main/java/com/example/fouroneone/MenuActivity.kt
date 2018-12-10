/*
* Placeholder activity to be replaced with a fancier design.
* */

package com.example.fouroneone

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.doAsync

class MenuActivity : AppCompatActivity() {

    private lateinit var fortuneCookieButton: Button
    private lateinit var spotifyButton: Button
    private lateinit var newsButton: Button
    private lateinit var weatherButton: Button
    private lateinit var weatherTemperatureText: TextView
    private lateinit var weatherDescriptionText: TextView
    private lateinit var fortuneCookieQuote: TextView
    private lateinit var animation: AnimationDrawable
    private lateinit var newsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val container = findViewById<ConstraintLayout>(R.id.container)
        animation = container.background as AnimationDrawable
        animation.setEnterFadeDuration(4000)
        animation.setExitFadeDuration(8000)

        fortuneCookieButton = findViewById(R.id.fortunecookie_button)
        spotifyButton = findViewById(R.id.spotify_button)
        newsButton = findViewById(R.id.menu_news_button)
        fortuneCookieButton = findViewById(R.id.fortunecookie_button)
        spotifyButton = findViewById(R.id.spotify_button)
        weatherButton = findViewById(R.id.menu_weather_button)
        weatherTemperatureText = findViewById(R.id.menu_weather_temperature)
        weatherDescriptionText = findViewById(R.id.menu_weather_description)
        fortuneCookieQuote = findViewById(R.id.menu_fortune_cookie_text)
        newsText = findViewById(R.id.menu_news_text)

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

        doAsync {
            NewsManager().getFirstArticle(
                    successCallback = { article ->
                        runOnUiThread{
                            newsText.text = article.Title
                        }
                    },
                    errorCallback = {exception ->
                        Log.d("MenuActivity", exception.message)
                    },
                    context = this@MenuActivity
            )
        }


        fortuneCookieButton.setOnClickListener {
            val intent = Intent(this, FortuneCookieActivity::class.java)
            startActivity(intent)
        }
        spotifyButton.setOnClickListener{
            val intent = Intent(this, SpotifyActivity::class.java)
            startActivity(intent)
        }
        newsButton.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }
        weatherButton.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        if(animation.isRunning){
            animation.stop()
        }
    }
    override fun onResume() {
        super.onResume()
        if(!animation.isRunning){
            animation.start()
        }
    }
}
