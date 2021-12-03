package com.gb.weatherapp.framework.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gb.weatherapp.AppState
import com.gb.weatherapp.R
import com.gb.weatherapp.databinding.FragmentHistoryBinding
import com.gb.weatherapp.framework.showSnackBar
import com.gb.weatherapp.framework.ui.adapters.HistoryAdapter
import org.koin.android.ext.android.inject

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by inject()
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() } // отложенное создание(после первого запроса)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        historyFragmentRecyclerview.adapter = adapter // в этом месте  будет создан адаптер
        viewModel.historyLiveData.observe(viewLifecycleOwner, { renderData(it) })// как только появятся данные вызывается метод renderData
        viewModel.getAllHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Success -> { //при удачной загрузке показываем список
                historyFragmentRecyclerview.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                adapter.setData(appState.weatherData) // сетим в адаптер данные
            }
            is AppState.Loading -> {
                historyFragmentRecyclerview.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                progressBar.visibility = View.GONE
                historyFragmentRecyclerview.showSnackBar( //показываем снек бар с ошибкой
                    getString(R.string.error),
                    getString(R.string.reload),
                    action = {
                        viewModel.getAllHistory()
                    })
            }
        }
    }

    companion object {//обьект который будет созавать фрагмент
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}