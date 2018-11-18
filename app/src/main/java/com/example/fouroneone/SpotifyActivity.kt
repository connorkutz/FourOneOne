package com.example.fouroneone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SpotifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)
    }

    override fun onStart() {
        super.onStart()
        // We will start writing our code here.
    }

    private fun connected() {
        // Then we will write some more code here.
    }

    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
    }
}
