package com.gb.weatherapp.framework.ui.details

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.weatherapp.AppState
import com.gb.weatherapp.model.repository.Repository

class DetailsViewModel(private val repository: Repository) : ViewModel(), LifecycleObserver {
    val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    fun loadData(lat: Double, lng: Double) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            val data = repository.getWeatherFromServer(lat, lng)// получаем наши данные
            // синхронизируемся с потоком UI  и сетим AppState.Success с нашими данными которые получены от сервера
            liveDataToObserve.postValue(AppState.Success(listOf(data)))
        }.start()
    }
}
