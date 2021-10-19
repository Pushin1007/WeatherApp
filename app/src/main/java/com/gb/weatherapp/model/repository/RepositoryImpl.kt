package com.gb.weatherapp.model.repository

import com.gb.weatherapp.model.WeatherLoader
import com.gb.weatherapp.model.entities.City
import com.gb.weatherapp.model.entities.Weather


class RepositoryImpl : Repository {

    override fun getWeatherFromServer(lat: Double, lng: Double): Weather {
        val dto = WeatherLoader.loadWeather(lat, lng) //грузим погоду с сервера
        return Weather(
            temperature = dto?.fact?.temp ?: 0,
            feelsLike = dto?.fact?.feels_like ?: 0,
            condition = dto?.fact?.condition
        )
    }

    override fun getWeatherFromLocalStorageRus() = City.getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = City.getWorldCities()
}