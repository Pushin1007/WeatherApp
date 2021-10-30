package com.gb.weatherapp.model.rest

import com.gb.weatherapp.model.rest_entities.WeatherDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("informers") // один метод который завершает формирование запроса
    fun getWeather(
        @Query("lat") lat: Double, //передаем параметры, которые будут вставлены после  informers
        @Query("lon") lon: Double
    ) : Call<WeatherDTO> //возврящаем объект  типа Call, который позволит делать ассинхронные запросы
}