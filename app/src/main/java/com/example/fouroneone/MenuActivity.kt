/*
* Placeholder activity to be replaced with a fancier design.
* */

package com.example.fouroneone

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuActivity : AppCompatActivity() {

    private lateinit var fortuneCookieButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        fortuneCookieButton = findViewById(R.id.fortunecookie_button)

        fortuneCookieButton.setOnClickListener {
            val intent = Intent(this, FortuneCookieActivity::class.java)
            startActivity(intent)
        }
    }
}
