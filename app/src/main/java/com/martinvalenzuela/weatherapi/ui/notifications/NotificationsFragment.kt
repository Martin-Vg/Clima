package com.martinvalenzuela.weatherapi.ui.notifications

import androidx.appcompat.app.AppCompatActivity
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
import com.martinvalenzuela.weatherapi.databinding.FragmentNotificationsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var tv_temp: TextView
    val apiKey = "6803232807d2db181ffffe13a6e68f31"
    var city = "Mexico"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tv_temp= binding.textNotifications
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