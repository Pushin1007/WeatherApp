package com.gb.weatherapp.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(foreignKeys = arrayOf(ForeignKey(entity = HistoryEntity::class, parentColumns = arrayOf("ds", "sd"), childColumns = )))
@Entity
data class HistoryEntity( //таблица базы данных с тремя полями
    @PrimaryKey(autoGenerate = true) val id: Long,
    val city: String,
    val temperature: Int,
    val condition: String
)