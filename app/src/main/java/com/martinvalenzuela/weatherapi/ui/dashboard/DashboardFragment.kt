package com.martinvalenzuela.weatherapi.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.martinvalenzuela.weatherapi.WeatherApiClient
import com.martinvalenzuela.weatherapi.WeatherResponse
import com.martinvalenzuela.weatherapi.databinding.FragmentDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var tv_temp: TextView
    val apiKey = "6803232807d2db181ffffe13a6e68f31"
    var city = "Tijuana"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tv_temp = binding.textDashboard
        search_weather()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun search_weather(){
        val call = WeatherApiClient.weatherService.getCurrentWeather(city, apiKey)

        call.enqueue(object: Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ){
                Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()
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
                Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_LONG).show()
            }
        })
    }
}