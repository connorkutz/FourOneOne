package com.example.fouroneone.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.example.fouroneone.managers.FortuneCookieManager
import com.example.fouroneone.R

class FortuneCookieActivity : AppCompatActivity() {

    private lateinit var quoteButton: Button
    private lateinit var quoteText: TextView
    private lateinit var adviceButton: Button
    private lateinit var adviceText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fortune_cookie)

        quoteButton = findViewById(R.id.quote_button)
        quoteText = findViewById(R.id.quote_text)
        adviceButton = findViewById(R.id.advice_button)
        adviceText = findViewById(R.id.advice_text)

        quoteButton.setOnClickListener {
            FortuneCookieManager().getQuote(
                    successCallback = {quote ->
                        runOnUiThread{
                            // TODO fix weird character formatting issues in quote
                            quoteText.text = quote
                        }
                    },
                    errorCallback = { exception ->
                        runOnUiThread{
                            quoteText.text = exception.message
                        }
                    }
            )
        }

        adviceButton.setOnClickListener {
            FortuneCookieManager().getAdvice(
                    successCallback = {advice ->
                        runOnUiThread{
                            adviceText.text = advice
                        }
                    },
                    errorCallback = { exception ->
                        runOnUiThread{
                            adviceText.text = exception.message
                        }
                    }
            )

        }
    }

}
