package com.gb.weatherapp.model.entities

import android.os.Parcelable
import com.gb.weatherapp.DEFAULF_CONDITION
import kotlinx.parcelize.Parcelize
import kotlin.collections.HashMap as HashMap1

@Parcelize
data class Weather( //делаем класс Parcelable для передачи
    val city: City = City.getDefaultCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0,
    val condition: String? = DEFAULF_CONDITION,


) : Parcelable


