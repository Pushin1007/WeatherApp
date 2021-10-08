package com.gb.weatherapp.model.repository

import com.gb.weatherapp.model.entities.Weather

class RepositoryImpl : Repository {

    override fun getWeatherFromServer() = Weather()

    override fun getWeatherFromLocalStorage() = Weather()
}