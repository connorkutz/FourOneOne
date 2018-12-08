package com.example.fouroneone

import android.content.Context
import android.text.Html
import com.spotify.protocol.client.ErrorCallback
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

class NewsManager{
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().let { builder ->
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)

        builder.build()
    }

    fun getNews(
            successCallback: (MutableList<Article>) -> Unit,
            errorCallback: (Exception) -> Unit,
            context: Context
    ){
        val api_key = context.getString(R.string.news_key)
        val request = Request.Builder()
                .url("'https://newsapi.org/v2/top-headlines?country=us&apiKey=$api_key")
                .build()

        // TODO add Firebase crashlytics

        okHttpClient.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(Exception("Unable to retrieve News."))
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString: String? = response.body()?.string()

                if(response.isSuccessful && responseString != null){
                    val articles : MutableList<Article> = arrayListOf()
                    val jsonArticles = JSONObject(responseString).getJSONArray("articles")
                    for(i in 0 until jsonArticles.length()){
                        val curr = jsonArticles.getJSONObject(i)
                        val article = Article(curr.getString("title"))
                        article.source = curr.getString("source.name")
                        article.url = curr.getString("url")
                        article.imageURL = curr.getString("urlToImage")
                        articles.add(article)
                    //val quoteObject = JSONArray(responseString).getJSONObject(0)
                    //val quoteBodyFull = quoteObject.getString("content")        // With <p> and </p> at either end
                    //val quoteBody = Html.fromHtml(quoteBodyFull, Html.FROM_HTML_MODE_COMPACT)

                    //val quoter = quoteObject.getString("title")
                    //val finalQuote = quoteBody.toString() + " -" + quoter
                    successCallback(articles)
                }
                else{
                    errorCallback(Exception("Unable to retrieve News."))
                }
            }
        })
    }
}