package com.gb.weatherapp.framework.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.gb.weatherapp.AppState
import com.gb.weatherapp.R
import com.gb.weatherapp.databinding.MainFragmentBinding
import com.gb.weatherapp.model.entities.Weather
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {


    private val viewModel: MainViewModel by viewModel() // потом иннициализируется в методе  viewModel()

    /*
    Биндинг-класс содержит ссылку на корневой макет.
    В нём также содержатся все ссылки на view макета, у которых прописаны id.
    Для замены метода findViewById
     */
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!! // Утверждаем что наше выражение не null и переопределяем метод  get()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root// root - это ссылка на корневой Layout
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //делаем подписку
        //Observer  - это класс который позволяет нам подписаться
        // на события когда мы получаем через LiveData() какой-то ивент
        val observer = Observer<AppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getWeatherFromLocalSourceRus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // обнуляем ссылку для на фрагмент

    }

    private fun renderData(appState: AppState) = with(binding) {
        //этот метод будет вызываться когда мы будем получать какой-то ивент от наших подписок
        when (appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                progressBar.visibility = View.GONE
                weatherGroup.visibility = View.VISIBLE
                setData(weatherData)
            }
            is AppState.Loading -> {
                weatherGroup.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                progressBar.visibility = View.GONE
                weatherGroup.visibility = View.INVISIBLE
                Snackbar
                    .make(mainView, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getWeatherFromLocalSourceRus() }
                    .show()
            }
        }
    }

    private fun setData(weatherData: Weather) = with(binding) {// сетим во вьюхи данные
        /*
         Выражение with(binding) позволяет упростить написние кода
          иначе первая строчка выглядела бы такЖ
           binding.cityName.text = weatherData.city.city
         */
        cityName.text = weatherData.city.city
        cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherData.city.lat.toString(),
            weatherData.city.lon.toString()
        )
        temperatureValue.text = weatherData.temperature.toString()
        feelsLikeValue.text = weatherData.feelsLike.toString()
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}