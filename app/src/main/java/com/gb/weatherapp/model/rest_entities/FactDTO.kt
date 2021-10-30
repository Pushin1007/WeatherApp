package com.gb.weatherapp.model.rest_entities

import com.google.gson.annotations.SerializedName

data class FactDTO(
    val temp: Int?,

    @SerializedName("feels_like") // используем аннотацию, для того чтобы в проекте были правильно названы переменные
    val feelsLike: Int?,

    val condition: String?
)
