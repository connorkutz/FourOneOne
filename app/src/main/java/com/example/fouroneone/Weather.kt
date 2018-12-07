package com.example.fouroneone

data class Weather(
        val temperature: Double,
        val description: String,
        val cityName: String,
        val pressure: Double,
        val windSpeed: Double,
        val feelsLike: Double,
        val clouds: Int
)