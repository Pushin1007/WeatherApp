package com.gb.weatherapp.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(  // тоже делаем его Parcelable, т.к. находится внутри Weather
    val city: String,
    val lat: Double,
    val lon: Double
): Parcelable
