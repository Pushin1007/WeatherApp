package com.gb.weatherapp.model.repository

import com.gb.weatherapp.model.entities.Weather

interface Repository {
    //два метода: забор погоды с сервера и забор погоды из локального хранилища
    fun getWeatherFromServer(lat: Double,lng: Double): Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}