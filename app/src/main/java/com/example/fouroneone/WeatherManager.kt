package com.example.fouroneone

import com.example.fouroneone.Weather
import com.spotify.protocol.client.ErrorCallback
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class WeatherManager{

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().let { builder ->
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)

        builder.build()
    }

    fun getCurrentWeather(
            apiKey: String,
            successCallback: (Weather) -> Unit,
            errorCallback: (Exception) -> Unit
    ){
        val request = Request.Builder()
                .url("https://api.weatherbit.io/v2.0/current?postal_code=20037&units=I&key=$apiKey")
                .build()

        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString: String? = response.body()?.string()

                if(response.isSuccessful && responseString != null){
                    val responseArray = JSONObject(responseString).getJSONArray("data")
                    val responseObject = responseArray.getJSONObject(0)
                    val temperature = responseObject.getDouble("temp")
                    val description = responseObject.getJSONObject("weather").getString("description")
                    val cityName = responseObject.getString("city_name")
                    val humidity = responseObject.getDouble("rh")
                    val windSpeed = responseObject.getDouble("wind_spd")
                    val feelsLike = responseObject.getDouble("app_temp")
                    val clouds = responseObject.getInt("clouds")
                    val weather = Weather(" " + temperature.toString() + " °F", description, " " + cityName, " " + humidity.toString() + " %", " " + windSpeed.toString() + "mph", " " + feelsLike.toString() + " °F", " " + clouds.toString() + "%")
                    successCallback(weather)
                }
                else{
                    errorCallback(Exception("Unable to retrieve weather information."))
                }
            }
        })
    }
}