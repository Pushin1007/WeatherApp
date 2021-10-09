package com.gb.weatherapp.di

import com.gb.weatherapp.framework.ui.main.MainViewModel
import com.gb.weatherapp.model.repository.Repository
import com.gb.weatherapp.model.repository.RepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //создаем синглтон один на все приложение
    single<Repository> { RepositoryImpl() }

    //View models
    // метод  (get() заставит обратиться коин и найти у своих модулей тип который нужно на вход во ViewModel
    viewModel { MainViewModel(get()) }
}