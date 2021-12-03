package com.gb.weatherapp.framework.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gb.weatherapp.databinding.ItemHistoryListBinding
import com.gb.weatherapp.model.entities.Weather

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() { //отрисовка элементов списка истории
    private var data: List<Weather> = arrayListOf()

    fun setData(data: List<Weather>) { //обновляем данные после появления вьюхи на экране
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder { //
        return RecyclerItemViewHolder(
            ItemHistoryListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class RecyclerItemViewHolder(private val binding: ItemHistoryListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Weather) = with(binding) { // заполнение строчки истории
            if (layoutPosition != RecyclerView.NO_POSITION) {
                recyclerViewItem.text =
                    String.format("%s %d %s", data.city.cityName, data.temperature, data.condition)
                root.setOnClickListener {
                    Toast.makeText(
                        itemView.context,
                        "on click: ${data.city.cityName}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}