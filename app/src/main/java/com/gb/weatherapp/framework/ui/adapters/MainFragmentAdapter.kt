package com.gb.weatherapp.framework.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gb.weatherapp.databinding.FragmentMainRecyclerItemBinding
import com.gb.weatherapp.framework.ui.main.MainFragment
import com.gb.weatherapp.model.entities.Weather

class MainFragmentAdapter(private val itemClickListener: MainFragment.OnItemViewClickListener) // получаем на вход слушателя который приходит от MainFragment
    : RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {
    private var weatherData: List<Weather> = listOf()
    private lateinit var binding: FragmentMainRecyclerItemBinding // объявляем  binding

    @SuppressLint("NotifyDataSetChanged") // когда придет список городов, будем перерисовывать весь список
    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    // переопередяем стандартные методы  RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        binding = FragmentMainRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MainViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount() = weatherData.size

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //  ViewHolder
        fun bind(weather: Weather) = with(binding) {
            mainFragmentRecyclerItemTextView.text =
                weather.city.city //сетим текст во вьюху из  weather
            root.setOnClickListener { itemClickListener.onItemViewClick(weather) } // принажатии на корневую вьюху мы передадим погоду
        }
    }
}