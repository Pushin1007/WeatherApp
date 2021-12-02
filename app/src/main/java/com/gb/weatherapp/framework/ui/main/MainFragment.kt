package com.gb.weatherapp.framework.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.gb.weatherapp.AppState
import com.gb.weatherapp.R
import com.gb.weatherapp.databinding.MainFragmentBinding
import com.gb.weatherapp.framework.ui.adapters.MainFragmentAdapter
import com.gb.weatherapp.framework.ui.details.DetailsFragment
import com.gb.weatherapp.model.entities.Weather
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.gb.weatherapp.BUNDLE_EXTRA
import com.gb.weatherapp.DATA_SET_KEY
import com.gb.weatherapp.framework.showSnackBar

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

    private val permissionResult = //метод проверки есть ли разрешение на геолокацию
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                getLocation()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.dialog_message_no_gps),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


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
            mainImageLocation.setOnClickListener { checkPermission() }
            viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
            viewModel.getWeatherFromLocalSourceRus()
        }
        loadDataSet()
        initDataSet()
    }


    //метод смены списка и картинки кнопки при выборе мировых/российских городов
    private fun changeWeatherDataSet() = with(binding) {
        isDataSetRus = !isDataSetRus
        initDataSet()
    }

    private fun loadDataSet() { // грузим значеие из настроек какой список выводить
        activity?.let {
            isDataSetRus = activity
                ?.getPreferences(Context.MODE_PRIVATE)
                ?.getBoolean(DATA_SET_KEY, true) ?: true// значение по дефолту
        }
    }

    private fun initDataSet() = with(binding) {// сетим значение в настройки и сохранем
        //функция расширения with() говорит о том что мы будем везти обьекта binding. Иначе пришлось бы писать  binding.viewModel.... и пр
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            mainImageButton.setImageResource(R.drawable.ic_earth)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            mainImageButton.setImageResource(R.drawable.ic_russia)
        }
        saveDataSetToDisk()
    }

    private fun saveDataSetToDisk() { // метод сохранения настроек
        val editor = activity?.getPreferences(Context.MODE_PRIVATE)?.edit() // открываем для записи
        editor?.putBoolean(DATA_SET_KEY, isDataSetRus) //кладем наше значение по ключу
        editor?.apply()

    }

    private fun checkPermission() { // метод запроса разрешения не геолокацию
        context?.let { notNullContext ->
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    notNullContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) -> {
                    //Доступ к местоположению на телефоне есть
                    getLocation()
                }
                else -> {
                    //Иначе запрашиваем разрешение
                    permissionResult.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun getLocation() { //запрашиваем наше местоположение и показываем нашу погоду
        activity?.let { context ->
            // Получить менеджер геолокаций
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                provider?.let {
                    // Будем получать геоположение через каждые 60 секунд или каждые 100 метров
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000,
                        10f,
                        onLocationListener
                    )
                }
            } else {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location == null) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.looks_like_location_disabled),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    getAddressAsync(location)
                }
            }
        }
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

                mainImageButton.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload)
                ) {
                    //функцию на вход в данном случае мы можем вынести за скобки
                    viewModel.getWeatherFromLocalSourceRus()
                }
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