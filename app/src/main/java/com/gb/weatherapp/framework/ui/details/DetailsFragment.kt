package com.gb.weatherapp.framework.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.gb.weatherapp.AppState
import com.gb.weatherapp.BUNDLE_EXTRA
import com.gb.weatherapp.DEFAULF_CONDITION
import com.gb.weatherapp.R
import com.gb.weatherapp.databinding.DetailsFragmentBinding
import com.gb.weatherapp.framework.toast
import com.gb.weatherapp.model.entities.Weather
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {
    /*
   Биндинг-класс содержит ссылку на корневой макет.
   В нём также содержатся все ссылки на view макета, у которых прописаны id.
   Для замены метода findViewById
    */
    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!  // Утверждаем что наше выражение не null и переопределяем метод  get()
    private val viewModel: DetailsViewModel by viewModel()
    val conditionRus: HashMap<String, String> = hashMapOf(
        "clear" to "ясно",
        "partly-cloudy" to "малооблачно",
        "cloudy" to "облачно с прояснениями",
        "overcast" to "пасмурно",
        "drizzle" to "морось",
        "light-rain" to "небольшой дождь",
        "rain" to "дождь",
        "moderate-rain" to "умеренно сильный дождь",
        "heavy-rain" to "сильный дождь",
        "continuous-heavy-rain" to "длительный сильный дождь",
        "showers" to "ливень",
        "wet-snow" to "дождь со снегом",
        "light-snow" to "небольшой снег",
        "snow" to "снег",
        "snow-showers" to "снегопад",
        "hail" to "град",
        "thunderstorm" to "гроза",
        "thunderstorm-with-rain" to "дождь с грозой",
        "thunderstorm-with-hail" to "гроза с градом"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root // root - это ссылка на корневой Layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.toast("Обновляем Свежие данные с сервера")//тут тост работает

        arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let {
/*
                 Забираем значения из аргументов, которые мы положили во фрагмент
                 Значения  типа  Weather в формате Parcelable
*/
            with(binding) { // сетим во вьюхи данные
/*
        Выражение with(binding) позволяет упростить написние кода
         иначе первая строчка выглядела бы так:
          binding.cityName.text = weatherData.city.city
*/

                imageView.load(it.city.cityImage)// сетим картинку
                cityName.text = it.city.cityName
                cityCoordinates.text = String.format(
                    getString(R.string.city_coordinates),
                    it.city.lat.toString(),
                    it.city.lon.toString()
                )
                viewModel.liveDataToObserve.observe(viewLifecycleOwner, { appState ->
                    when (appState) {
                        is AppState.Success -> {
                            loadingLayout.visibility = View.GONE
                            mainView.visibility = View.VISIBLE
                            temperatureValue.text = appState.weatherData[0].temperature.toString()
                            feelsLikeValue.text = appState.weatherData[0].feelsLike.toString()
                            weatherCondition.text = if (appState.weatherData[0].condition == null) {
                                DEFAULF_CONDITION
                            } else {
                                conditionRus.getValue(appState.weatherData[0].condition.toString())
                            }

                        }
                        is AppState.Loading -> {
                            mainView.visibility = View.INVISIBLE
                            binding.loadingLayout.visibility = View.GONE
                        }
                        is AppState.Error -> {
                            mainView.visibility = View.INVISIBLE
                            loadingLayout.visibility = View.GONE
                            errorTV.visibility = View.VISIBLE
                            context?.toast("Ошибка загрузки")
                        }
                    }
                })
                viewModel.loadData(it.city)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // обнуляем ссылку  на фрагмент
    }

    companion object {

        fun newInstance(bundle: Bundle): DetailsFragment { //в Bundle будет лежать погода типа  Weather
            val fragment = DetailsFragment()
            fragment.arguments = bundle // фрагмент будет на входе уже со значениями
            return fragment
        }
    }
}