package com.gb.weatherapp.model

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext

import com.gb.weatherapp.model.rest_entities.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection
import com.gb.weatherapp.BuildConfig
import com.gb.weatherapp.MainActivity
import com.gb.weatherapp.READ_TIMEOUT
import com.google.android.material.snackbar.Snackbar

object WeatherLoader {
    // класс загрузчик
    fun loadWeather(lat: Double, lon: Double): WeatherDTO? {
        try {
            //https запрос в котором мы отправляем данные о широте и долготе
            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}&[lang=ru_RU]")
            lateinit var urlConnection: HttpsURLConnection // HttpsURLConnection класс который позволет подключиться к url
            try {
                urlConnection =
                    uri.openConnection() as HttpsURLConnection // делаем каст к HttpsURLConnection т.к. возвращается httpConnection
                urlConnection.requestMethod = "GET" // Сетим в него метод получения данных-- GET
                urlConnection.addRequestProperty(
                    "X-Yandex-API-Key",
                    BuildConfig.WEATHER_API_KEY //
                )

                urlConnection.readTimeout = READ_TIMEOUT  //установка таймаута — 10 000 миллисекунд

                /*
                читаем данные в поток.
                при вызове inputStream мы обращемся к серверу
                оборочиваем в InputStreamReader чтобы можно было прочитать данные
                оборочиваем в BufferedReader чтобы ридер клал данные в буфер
                 */
                val bufferedReader =
                    BufferedReader(InputStreamReader(urlConnection.inputStream))// способ получения запроса
                // преобразование ответа от сервера (JSON) в модель данных
                //2 два варианта разбора ответов
                val lines = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    getLinesForOld(bufferedReader) //способ разбора для 6 Android и ниже
                } else {
                    getLines(bufferedReader)   //способ разбора для 7 Android и выше
                }
                // передаем наш JSON ответ и говорим в какой объект он должен его распарсить
                return Gson().fromJson(lines, WeatherDTO::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                urlConnection.disconnect()// отключаемся от urlConnection
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    interface WeatherLoaderErrorListener {
        fun showError(throwable: Throwable)
    }

    private fun getLinesForOld(reader: BufferedReader): String { // из всех данных которые получаем собираем в строку старым способом
        val rawData = StringBuilder(1024) //емкость строчки
        var tempVariable: String?
        while (reader.readLine().also { tempVariable = it } != null) {
            //читаем каждую строчку, сетим ее в переменную tempVariable отделяя знаком  "\n"
            rawData.append(tempVariable).append("\n")
        }
        reader.close()
        return rawData.toString()
    }

    @RequiresApi(Build.VERSION_CODES.N) // из всех данных которые получаем собираем в строку
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

}
