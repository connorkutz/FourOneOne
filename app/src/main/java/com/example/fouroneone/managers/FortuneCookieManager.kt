package com.example.fouroneone.managers

import android.text.Html
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

class FortuneCookieManager {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().let { builder ->
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)

        builder.build()
    }

    fun getQuote(
            successCallback: (String) -> Unit,
            errorCallback: (Exception) -> Unit
    ){
        val request = Request.Builder()
                .url("http://quotesondesign.com/wp-json/posts?filter[orderby]=rand&filter[posts_per_page]=1")
                .build()

        okHttpClient.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(Exception("Unable to retrieve quote."))
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString: String? = response.body()?.string()

                if(response.isSuccessful && responseString != null){
                    val quoteObject = JSONArray(responseString).getJSONObject(0)
                    val quoteBodyFull = quoteObject.getString("content")        // With <p> and </p> at either end
                    val quoteBody = Html.fromHtml(quoteBodyFull, Html.FROM_HTML_MODE_COMPACT)

                    val quoter = quoteObject.getString("title")
                    val finalQuote = quoteBody.toString() + " -" + quoter
                    successCallback(finalQuote)
                }
                else{
                    errorCallback(Exception("Unable to retrieve quote."))
                }
            }
        })
    }

    fun getAdvice(
            successCallback: (String) -> Unit,
            errorCallback: (Exception) -> Unit){
        val request = Request.Builder()
                .url("https://api.adviceslip.com/advice")
                .build()

        // TODO add Firebase crashlytics

        okHttpClient.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(Exception("Unable to retrieve advice."))
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body()?.string()

                if(response.isSuccessful && responseString != null){
                    val advice = JSONObject(responseString)
                    val adviceObject = advice.getJSONObject("slip")
                    val adviceBody = adviceObject.getString("advice")
                    successCallback(adviceBody)
                }
                else{
                    errorCallback(Exception("Unable to retrieve advice."))
                }
            }
        })

    }
}