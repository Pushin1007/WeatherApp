package com.gb.weatherapp.framework.ui.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.gb.weatherapp.AppState
import com.gb.weatherapp.model.repository.Repository
import com.google.android.material.snackbar.Snackbar
import java.lang.NullPointerException
import java.security.acl.NotOwnerException
import java.lang.IllegalArgumentException

class MainViewModel(private val repository: Repository) : ViewModel() {
    // MutableLiveData в отличии от LiveData позволяет себя изменить и пушить в нее данные
    private val liveData = MutableLiveData<AppState>()

    fun getLiveData(): LiveData<AppState> = liveData //переопределяем геттер для liveData

    fun getWeather() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {// заглушка для получения погоды
        liveData.value = AppState.Loading
        Thread {
            Thread.sleep(1000)

            try {
                randomLoad()
            } catch (e: Exception) {

            }

        }.start()
    }

    private fun randomLoad() {//случайное событие загрузки
        val rnds = (0..1).random()
        when (rnds) {
            0 -> liveData.postValue(AppState.Success(repository.getWeatherFromLocalStorage()))
            1 -> liveData.postValue(AppState.Error(throw  Exception()))
        }

    }


}