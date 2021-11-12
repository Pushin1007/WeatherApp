package com.gb.weatherapp.framework.ui.details

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gb.weatherapp.AppState
import com.gb.weatherapp.model.WeatherLoader
import com.gb.weatherapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.MalformedURLException

class DetailsViewModel(private val repository: Repository) : ViewModel(), LifecycleObserver,
    WeatherLoader.WeatherLoaderErrorListener {
    val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun loadData(lat: Double, lng: Double) {
        liveDataToObserve.value = AppState.Loading

        Thread { //зачем тут отдельный поток?
            viewModelScope.launch(Dispatchers.IO) {// переделываем обычный поток на корутины
                // viewModelScope интегрирован в ViewModel и его не нужно отменять
                val data = repository.getWeatherFromServer(lat, lng, this) // Почему тут this? в методе getWeatherFromServer контекста нет?
                // получаем наши данные
                // синхронизируемся с потоком UI  и сетим AppState.Success с нашими данными которые получены от сервера
                liveDataToObserve.postValue(AppState.Success(listOf(data)))
            }.start()
        }
    }

    override fun showError(throwable: Throwable) {
        liveDataToObserve.postValue(AppState.Error(error))// непонятно что тут надо передать??
    }
}

