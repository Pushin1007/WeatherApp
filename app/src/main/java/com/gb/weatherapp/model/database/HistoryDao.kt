package com.gb.weatherapp.model.database

import androidx.room.*

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryEntity")
    fun all(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE city LIKE :city")
    fun getDataByWord(city: String): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: HistoryEntity)

    @Update // обновление по PrimaryKey
    fun update(entity: HistoryEntity)

    @Delete // удаление по PrimaryKey
    fun delete(entity: HistoryEntity)

    // удаление по выборке определенного города
    @Query("DELETE FROM HistoryEntity WHERE city = :cityName")
    fun deleteByCityName(cityName: String)
}