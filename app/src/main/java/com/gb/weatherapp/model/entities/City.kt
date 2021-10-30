package com.gb.weatherapp.model.entities

import android.os.Parcelable
import com.gb.weatherapp.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(  // тоже делаем его Parcelable, т.к. находится внутри Weather
    val cityName: String,
    val cityImage: Int,
    val lat: Double,
    val lon: Double
) : Parcelable {
    companion object {
        fun getDefaultCity() = City("Москва", R.drawable.moskva, 55.755826, 37.617299900000035)

        fun getWorldCities() = listOf( //не изменяемый список иностранных городов
            Weather(City("Лондон", R.drawable.london, 51.5085300, -0.1257400), 1, 2),
            Weather(City("Токио", R.drawable.tokyo, 35.6895000, 139.6917100), 3, 4),
            Weather(City("Париж", R.drawable.paris, 48.8534100, 2.3488000), 5, 6),
            Weather(City("Берлин", R.drawable.berlin, 52.52000659999999, 13.404953999999975), 7, 8),
            Weather(City("Рим", R.drawable.roma, 41.9027835, 12.496365500000024), 9, 10),
            Weather(City("Минск", R.drawable.minsk, 53.90453979999999, 27.561524400000053), 11, 12),
            Weather(City("Стамбул", R.drawable.istanbul, 41.0082376, 28.97835889999999), 13, 14),
            Weather(City("Вашингтон", R.drawable.washington, 38.9071923, -77.03687070000001), 15, 16),
            Weather(City("Киев", R.drawable.kiev, 50.4501, 30.523400000000038), 17, 18),
            Weather(City("Донецк", R.drawable.donetsk, 48.002777, 37.805279), 19, 20),
            Weather(City("Соледар", R.drawable.soledar, 48.67935, 38.08902), 19, 20),
            Weather(City("Пекин", R.drawable.pekin, 39.90419989999999, 116.40739630000007), 19, 20)

        )

        fun getRussianCities() = listOf( //не изменяемый список российских городов
            Weather(City("Москва", R.drawable.moskva, 55.755826, 37.617299900000035), 1, 2),
            Weather(City("Санкт-Петербург", R.drawable.spb, 59.9342802, 30.335098600000038), 3, 3),
            Weather(City("Новосибирск", R.drawable.novosibirsk, 55.00835259999999, 82.93573270000002), 5, 6),
            Weather(City("Екатеринбург", R.drawable.yekaterinburg, 56.83892609999999, 60.60570250000001), 7, ),
            Weather(City("Нижний Новгород", R.drawable.nizhny_novgorod, 56.2965039, 43.936059), 9, 10),
            Weather(City("Казань", R.drawable.kazan, 55.8304307, 49.06608060000008), 11, 12),
            Weather(City("Челябинск", R.drawable.chelyab, 55.1644419, 61.4368432), 13, 14),
            Weather(City("Омск", R.drawable.omsk, 54.9884804, 73.32423610000001), 15, 16),
            Weather(City("Ростов-на-Дону", R.drawable.rostov_on_don, 47.2357137, 39.701505), 17, 18),
            Weather(City("Уфа", R.drawable.ufa, 54.7387621, 55.972055400000045), 19, 20))
    }


}
