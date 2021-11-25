package com.gb.weatherapp.model.repository

import com.gb.weatherapp.R
import com.gb.weatherapp.model.WeatherLoader
import com.gb.weatherapp.model.database.Database
import com.gb.weatherapp.model.database.HistoryEntity
import com.gb.weatherapp.model.entities.City
import com.gb.weatherapp.model.entities.Weather


class RepositoryImpl : Repository {

    override fun getWeatherFromServer(
        lat: Double,
        lng: Double,
        listener: WeatherLoader.WeatherLoaderErrorListener
    ): Weather? {
        val dto = WeatherLoader.loadWeather(lat, lng, listener) //грузим погоду с сервера
        //грузим погоду в нужном нам потоке (синхронный запрос) с сервера с помощью retrofit
        //val dto = WeatherRepo.api.getWeather(lat, lng).execute().body()
        return if (dto == null) {
            null
        } else Weather(
            temperature = dto.fact.temp ?: 0,
            feelsLike = dto.fact.feelsLike ?: 0,
            condition = dto.fact.condition
        )
    }

    override fun getWeatherFromLocalStorageRus() = City.getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = City.getWorldCities()

    override fun saveEntity(weather: Weather) {
        Database.db.historyDao().insert(convertWeatherToEntity(weather))
    }

    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(Database.db.historyDao().all())
    }

    private fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> { //Конвертер  HistoryEntity в List<Weather>
        return entityList.map {
            Weather(City(it.city, 0, 0.0, 0.0), it.temperature, 0, it.condition)
        }
    }


    private fun convertWeatherToEntity(weather: Weather): HistoryEntity { //Конвертер   Weather в  HistoryEntity
        return HistoryEntity(
            0, weather.city.cityName,
            weather.temperature,
            weather.condition ?: ""
        )
    }


}