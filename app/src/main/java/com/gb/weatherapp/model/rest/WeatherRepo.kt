package com.gb.weatherapp.model.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRepo {
    val api: WeatherAPI by lazy { // "ленивая инициализация" - Будем создавать объект при первом доступе
        val adapter = Retrofit.Builder()
            .baseUrl(ApiUtils.baseUrl) // вставляем  базовый URL
            .addConverterFactory(GsonConverterFactory.create()) // вставляем конвертер
            .client(ApiUtils.getOkHTTPBuilderWithHeaders()) //вставляем наш клиент
            .build()

        adapter.create(WeatherAPI::class.java)
    }
}