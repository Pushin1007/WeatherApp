package com.gb.weatherapp.model.rest

import com.gb.weatherapp.BuildConfig
import com.gb.weatherapp.TIMEOUT10С
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ApiUtils {
    private val baseUrlMainPart = "https://api.weather.yandex.ru/"  // Главная часть  базового Url
    private val baseUrlVersion = "v2/" // версия  API
    val baseUrl = "$baseUrlMainPart$baseUrlVersion" // базовый Url

    fun getOkHTTPBuilderWithHeaders(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(TIMEOUT10С, TimeUnit.SECONDS)// Время подключения к API не более секунд
        httpClient.readTimeout(TIMEOUT10С, TimeUnit.SECONDS) // Время на чтение данных не более секунд
        httpClient.writeTimeout(TIMEOUT10С, TimeUnit.SECONDS) // Время на отправку данных не более секунд
        httpClient.addInterceptor { chain -> //Interceptor позволяет изменить запрос
            val original = chain.request() // получаем запрос
            val request = original.newBuilder()
                .header("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY) //изменяем у него  header
                .method(original.method(), original.body())
                .build()

            chain.proceed(request) // отправляем уже измененный запрос
        }

        return httpClient.build()
    }
}