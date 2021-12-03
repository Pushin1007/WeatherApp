package com.gb.weatherapp.model.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.gb.weatherapp.App
import com.gb.weatherapp.DB_NAME

@androidx.room.Database(
    entities = [HistoryEntity::class],// какие таблицы учавствуют в нашей базе данных
    version = 1, //версия базы данных
    exportSchema = false //
)
abstract class Database : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {


        val db: Database by lazy {
            Room.databaseBuilder( //создаем базу данных
                App.appInstance, // указываем контекст
                Database::class.java,
                DB_NAME
            ).build()
        }
    }
}