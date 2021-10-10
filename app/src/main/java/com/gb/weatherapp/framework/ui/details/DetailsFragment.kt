package com.gb.weatherapp.framework.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gb.weatherapp.BUNDLE_EXTRA
import com.gb.weatherapp.R
import com.gb.weatherapp.model.entities.Weather
import com.gb.weatherapp.databinding.DetailsFragmentBinding

class DetailsFragment : Fragment() {
    /*
   Биндинг-класс содержит ссылку на корневой макет.
   В нём также содержатся все ссылки на view макета, у которых прописаны id.
   Для замены метода findViewById
    */
    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!  // Утверждаем что наше выражение не null и переопределяем метод  get()

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
                val city = it.city
                cityName.text = city.city
                cityCoordinates.text = String.format(
                    getString(R.string.city_coordinates),
                    city.lat.toString(),
                    city.lon.toString()
                )
                temperatureValue.text = it.temperature.toString()
                feelsLikeValue.text = it.feelsLike.toString()
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