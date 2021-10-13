package com.gb.weatherapp.framework.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gb.weatherapp.AppState
import com.gb.weatherapp.R
import com.gb.weatherapp.databinding.MainFragmentBinding
import com.gb.weatherapp.framework.ui.adapters.MainFragmentAdapter
import com.gb.weatherapp.framework.ui.details.DetailsFragment
import com.gb.weatherapp.model.entities.Weather
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.gb.weatherapp.BUNDLE_EXTRA

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModel()
    // by - делегирование. Мы  делегируем создание  MainViewModel в методе  viewModel() coin

    /*
    Биндинг-класс содержит ссылку на корневой макет.
    В нём также содержатся все ссылки на view макета, у которых прописаны id.
    Для замены метода findViewById
     */
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!! // Утверждаем что наше выражение не null и переопределяем метод  get()
    private var adapter: MainFragmentAdapter? = null //Адаптер для RecyclerView
    private var isDataSetRus: Boolean = true //первоначальная загрузка российских городов

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root// возвращаем корневое значение binding как элемент
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            mainFragmentRecyclerView.adapter = adapter //
            mainImageButton.setOnClickListener { changeWeatherDataSet() }
            viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
            viewModel.getWeatherFromLocalSourceRus()
        }
    }


    //метод смены списка и картинки кнопки при выборе мировых/российских городов
    private fun changeWeatherDataSet() = with(binding) {
        //функция расширения with() говорит о том что мы будем везти обьекта binding. Иначе пришлось бы писать  binding.viewModel.... и пр
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            mainImageButton.setImageResource(R.drawable.ic_earth)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            mainImageButton.setImageResource(R.drawable.ic_russia)
        }
        isDataSetRus = !isDataSetRus
    }


    private fun renderData(appState: AppState) = with(binding) {
        //этот метод будет вызываться когда мы будем получать какой-то ивент от наших подписок
        when (appState) {
            is AppState.Success -> { //если загрузка прошла ОК
                mainFragmentLoadingLayout.visibility = View.GONE //скрываем виджет загрузки
                adapter = MainFragmentAdapter(object : OnItemViewClickListener { //создаем адаптер
                    override fun onItemViewClick(weather: Weather) { //передаем ему реакцию от слушателя на один из жлементов списка
                        val manager = activity?.supportFragmentManager
                        manager?.let { manager ->
                            /*
                            .let - функция расширения которая возвращает  результат переданной функции, передав в нее объект на котором она вызвана
                            В частности здесь  функция возвращает объект уже проверенный на nullable.
                            и в последствии с ним уже можно будет работатть как с не  nullable объектом
                             */
                            val bundle = Bundle().apply {
                                /*
                                .apply - функция расширения которая позволяет на уже созданном объекте вызвать его методы без ссылки на сам объект
                                Позволяет соеденить создание объекта с его инициализацией
                                 */
                                putParcelable(BUNDLE_EXTRA, weather)
                            }
                            manager.beginTransaction()
                                .add(R.id.container, DetailsFragment.newInstance(bundle))
                                .addToBackStack("")
                                .commitAllowingStateLoss()
                        }
                    }
                }).apply {
                    setWeather(appState.weatherData)
                }
                mainFragmentRecyclerView.adapter = adapter
            }
            is AppState.Loading -> {  //если идет загрузка то просто  показываем виджет загрузки
                mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                mainFragmentLoadingLayout.visibility = View.GONE
                Snackbar
                    .make(
                        binding.mainImageButton,
                        getString(R.string.error),
                        Snackbar.LENGTH_INDEFINITE
                    )
                    .setAction(getString(R.string.reload)) { viewModel.getWeatherFromLocalSourceRus() }
                    .show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // обнуляем ссылку для на фрагмент

    }

    interface OnItemViewClickListener { //создаем интерфейс для того чтобы отлеживание на нажатие можно было пробросить дальше в адаптер
        fun onItemViewClick(weather: Weather)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}