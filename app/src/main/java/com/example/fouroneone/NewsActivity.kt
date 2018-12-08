package com.example.fouroneone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        NewsManager().getNews(
                successCallback = {quote ->
                    runOnUiThread{
                        // TODO fix weird character formatting issues in quote
                        //quoteText.text = quote
                    }
                },
                errorCallback = { exception ->
                    runOnUiThread{
                        //quoteText.text = exception.message
                    }
                }
        )
    }
}
