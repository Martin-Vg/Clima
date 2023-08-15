package com.martinvalenzuela.weatherapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var tv_temp: TextView
    lateinit var et_city: EditText
    lateinit var  btn_buscar: Button
    val apiKey = "6803232807d2db181ffffe13a6e68f31"
    var city = "Tijuana"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_temp = findViewById(R.id.tv_temperatura)
        et_city = findViewById(R.id.txt_id)
        btn_buscar = findViewById(R.id.btn_buscar)

        btn_buscar.setOnClickListener{
            city = et_city.text.toString()
            search_weather()
        }
    }

    private fun search_weather(){
        val call = WeatherApiClient.weatherService.getCurrentWeather(city, apiKey)

        call.enqueue(object: Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ){
                Toast.makeText(applicationContext, response.message(), Toast.LENGTH_LONG).show()
                if(response.isSuccessful){
                    val weather = response.body()
                    tv_temp.text = "Temperature: ${weather?.main?.temp}°C \n" +
                            "Humedad: ${weather?.main?.humidity} \n" +
                            "Maxima: ${weather?.main?.temp_max}°C \n" +
                            "Minima: ${weather?.main?.temp_min}°C \n" +
                            "Descripcion: ${weather?.weather?.last()?.description}"
                }else{
                    Log.e("MainActivity", response.message())
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Ocurrio un error", Toast.LENGTH_LONG).show()
            }
        })
    }
}