package com.zmitrovich.meteostation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.zmitrovich.meteostation.data.model.getFirstEntry
import com.zmitrovich.meteostation.databinding.MainFragmentBinding
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, null, false)
        setupEventObserver()
        return binding.root
    }

    private fun setupEventObserver() {
        viewModel.eventsFlow
            .onEach {
                when (it) {
                    is MainViewModel.Event.Loading -> showProgressBar()
                    is MainViewModel.Event.WeatherEvent -> {
                        when {
                            it.data.isSuccess -> setData(it.weatherType, it.data.getOrNull()!!)
                            it.data.isFailure -> showError()
                        }
                    }
                }
            }
    }

    private fun setData(weatherType: WeatherType, data: Map<String, Map<Date, Float>>) {
        setupXAxis(data.getFirstEntry().value.keys.toList())
        val dataSets = mutableListOf<ILineDataSet>()
        for ((label, XY) in data) {
            dataSets.add(
                LineDataSet(XY.values.mapIndexed { index, value ->
                    Entry(
                        index.toFloat(),
                        value
                    )
                }, label)
            )
        }
        binding.lcMain.data = LineData(dataSets)
        binding.lcMain.invalidate()
    }

    private fun setupXAxis(datesList: List<Date>) {
        val formatter: ValueFormatter = object : ValueFormatter() {

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                return simpleDateFormat.format(datesList[value.toInt()])
            }
        }

        binding.lcMain.xAxis.valueFormatter = formatter
    }


}