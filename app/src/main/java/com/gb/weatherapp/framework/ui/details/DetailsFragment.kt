package com.gb.weatherapp.framework.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gb.weatherapp.AppState
import com.gb.weatherapp.BUNDLE_EXTRA
import com.gb.weatherapp.R
import com.gb.weatherapp.model.entities.Weather
import com.gb.weatherapp.databinding.DetailsFragmentBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root // root - это ссылка на корневой Layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let {
/*
                 Забираем значения из аргументов, которые мы положили во фрагмент
                 Значения  типа  Weather в формате Parcelable
*/
            with(binding) { // сетим во вьюхи данные
/*
        Выражение with(binding) позволяет упростить написние кода
         иначе первая строчка выглядела бы такЖ
          binding.cityName.text = weatherData.city.city
*/

                cityName.text = it.city.city
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
                            weatherCondition.text = appState.weatherData[0].condition

                        }
                        is AppState.Loading -> {
                            mainView.visibility = View.INVISIBLE
                            binding.loadingLayout.visibility = View.GONE
                        }
                        is AppState.Error -> {
                            mainView.visibility = View.INVISIBLE
                            loadingLayout.visibility = View.GONE
                            errorTV.visibility = View.VISIBLE
                        }
                    }
                })
                viewModel.loadData(it.city.lat, it.city.lon)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // обнуляем ссылку для на фрагмент
    }

    companion object {

        fun newInstance(bundle: Bundle): DetailsFragment { //в Bundle будет лежать погода типа  Weather
            val fragment = DetailsFragment()
            fragment.arguments = bundle // фрагмент будет на входе уже со значениями
            return fragment
        }
    }
}