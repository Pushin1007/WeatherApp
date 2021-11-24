package com.gb.weatherapp.model.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.gb.weatherapp.App

@androidx.room.Database(
    entities = [HistoryEntity::class],// какие таблицы учавствуют в нашей базе данных
    version = 1, //версия базы данных
    exportSchema = false //
)
abstract class Database : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        private const val DB_NAME = "add_database.db"

        val db: Database by lazy {
            Room.databaseBuilder(
                App.appInstance,
                Database::class.java,
                DB_NAME
            ).build()
        }
    }
}