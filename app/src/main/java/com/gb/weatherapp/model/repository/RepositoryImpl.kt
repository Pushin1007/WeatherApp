package com.gb.weatherapp.model.repository

import com.gb.weatherapp.model.entities.City
import com.gb.weatherapp.model.entities.Weather


class RepositoryImpl : Repository {

    override fun getWeatherFromServer() = Weather()

    override fun getWeatherFromLocalStorageRus() = City.getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = City.getWorldCities()
}