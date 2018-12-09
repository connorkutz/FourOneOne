/*
* Placeholder activity to be replaced with a fancier design.
* */

package com.example.fouroneone

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.widget.Button

class MenuActivity : AppCompatActivity() {

    private lateinit var fortuneCookieButton: Button
    private lateinit var spotifyButton: Button
    private lateinit var newsButton: Button
    private lateinit var animation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val container = findViewById<ConstraintLayout>(R.id.container)
        animation = container.background as AnimationDrawable
        animation.setEnterFadeDuration(4000)
        animation.setExitFadeDuration(8000)

        fortuneCookieButton = findViewById(R.id.fortunecookie_button)
        spotifyButton = findViewById(R.id.spotify_button)
        newsButton = findViewById(R.id.news_button)


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
