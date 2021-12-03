package com.gb.weatherapp.framework.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import com.gb.weatherapp.R
import com.gb.weatherapp.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.IOException

class MapsFragment : Fragment(), CoroutineScope by MainScope() {
    private lateinit var map: GoogleMap
    private var menu: Menu? = null
    private val markers: ArrayList<Marker> = ArrayList() // ставим на карту маркеры
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap // сохраняем карту в переменную

        map.uiSettings.isZoomControlsEnabled = true // включаем кнопку зума
        map.uiSettings.isMyLocationButtonEnabled = true // включаем кнопку моего местоположения

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION //проверяем наличие геолокации
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        }

        val initialPlace = LatLng(44.952117, 34.102417) // начальное местоположение
        val marker = googleMap.addMarker(
            MarkerOptions()
                .position(initialPlace) //стартовый маркер на нашем местоположении
                .title(getString(R.string.start_marker)) //стартовый маркер под названием старт
        )
        marker?.let { markers.add(it) } //добавляем маркер в наш список маркеров
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialPlace)) // переход карты в наше начальное положение
        googleMap.setOnMapLongClickListener { latLng ->
            setMarker(latLng, "From long click") // рисуем линии при долгом нажатии
            drawLine()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback) //ассинхронный вызов
        initSearchByAddress()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.map_menu, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_map_mode_normal -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL //основная информация
                return true
            }
            R.id.menu_map_mode_satellite -> {
                map.mapType = GoogleMap.MAP_TYPE_SATELLITE //спутники
                return true
            }
            R.id.menu_map_mode_terrain -> { //
                map.mapType = GoogleMap.MAP_TYPE_TERRAIN // вид со спутника
                return true
            }
            R.id.menu_map_traffic -> { //включение пробок
                map.isTrafficEnabled = !map.isTrafficEnabled
                return true
            }
        }

        return false
    }

    private fun initSearchByAddress() = with(binding) {// метод поиск адреса
        buttonSearch.setOnClickListener {
            val geoCoder = Geocoder(it.context) // из текста получаем координаты
            val searchText = searchAddress.text.toString()
            launch(Dispatchers.IO) {
                try {
                    val addresses = geoCoder.getFromLocationName(searchText, 1)
                    if (addresses.isNotEmpty()) { // проверяем что поле не пустое
                        goToAddress(addresses, searchText) // переходим по адрему
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun goToAddress(
        addresses: MutableList<Address>,
        searchText: String
    ) { //переход к найденному адресу
        launch {
            val location = LatLng(addresses[0].latitude, addresses[0].longitude)
            setMarker(location, searchText)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))// зуммируем к адресу
        }
    }

    private fun drawLine() { // рисуем линии между маркерами
        val last: Int = markers.size - 1 // берем последний маркер
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline( // добавляем на карту полилинию
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.RED) //цвет линии
                    .width(5f) // ширина линии
            )
        }
    }

    private fun setMarker(
        location: LatLng,
        searchText: String
    ) { //ставим новый маркер при длительном нажатии
        map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
        )?.let { markers.add(it) }// добавляем с список наш маркер
    }

    companion object {
        fun newInstance() = MapsFragment()
    }
}