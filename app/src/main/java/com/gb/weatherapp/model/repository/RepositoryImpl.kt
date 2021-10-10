package com.gb.weatherapp.model.repository

import com.gb.weatherapp.model.entities.Weather
import com.gb.weatherapp.model.entities.getRussianCities
import com.gb.weatherapp.model.entities.getWorldCities

class RepositoryImpl : Repository {

    override fun getWeatherFromServer() = Weather()

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}