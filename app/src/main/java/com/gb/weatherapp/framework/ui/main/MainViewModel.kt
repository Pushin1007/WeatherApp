package com.gb.weatherapp.framework.ui.main

import androidx.lifecycle.*
import com.gb.weatherapp.AppState
import com.gb.weatherapp.COUNT_TRY
import com.gb.weatherapp.model.repository.Repository

class MainViewModel(private val repository: Repository) : ViewModel() {
    // MutableLiveData в отличии от LiveData позволяет себя изменить и пушить в нее данные
    private val liveData = MutableLiveData<AppState>()

    fun getLiveData(): LiveData<AppState> = liveData //переопределяем геттер для liveData

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(false)

    private fun getDataFromLocalSource(isRussian: Boolean) {// получение погоды
        liveData.value = AppState.Loading
        Thread {
            Thread.sleep(1000)

            // пробуем до тех пор пока не получится загрузка
            // чтобы не попасть в бутлуп пробуем 10 раз
            var countTry = COUNT_TRY
            do {
                var error = false
                countTry--
                try {
                    randomLoad(isRussian)
                } catch (e: Exception) {
                    error = true
                }
            } while (error && countTry >= 0)


        }.start()
    }

    private fun randomLoad(isRussian: Boolean) {//случайное событие загрузки
        val rnds = (0..1).random()
        when (rnds) {

            0 -> liveData.postValue(
                if (isRussian) {
                    AppState.Success(repository.getWeatherFromLocalStorageRus())
                } else {
                    AppState.Success(repository.getWeatherFromLocalStorageWorld())
                }
            )
            1 -> liveData.postValue(AppState.Error(throw  Exception()))
        }

    }


}