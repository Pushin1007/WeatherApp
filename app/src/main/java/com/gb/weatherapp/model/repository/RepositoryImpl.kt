package com.gb.weatherapp.model.repository

import com.gb.weatherapp.model.WeatherLoader
import com.gb.weatherapp.model.entities.City
import com.gb.weatherapp.model.entities.Weather
import com.gb.weatherapp.model.rest.WeatherRepo


class RepositoryImpl : Repository {

    override fun getWeatherFromServer(lat: Double, lng: Double): Weather {
//        val dto = WeatherLoader.loadWeather(lat, lng) //грузим погоду с сервера
        //грузим погоду в нужном нам потоке (синхронный запрос) с сервера с помощью retrofit
        val dto = WeatherRepo.api.getWeather(lat, lng).execute().body()
        return Weather(
            temperature = dto?.fact?.temp ?: 0,
            feelsLike = dto?.fact?.feelsLike ?: 0,
            condition = dto?.fact?.condition
        )
    }

    override fun getWeatherFromLocalStorageRus() = City.getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = City.getWorldCities()
}