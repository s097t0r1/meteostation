package com.zmitrovich.meteostation.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.zmitrovich.meteostation.data.model.getFirstEntry
import com.zmitrovich.meteostation.data.model.parameters.WeatherInterval
import com.zmitrovich.meteostation.data.model.parameters.WeatherParameters
import com.zmitrovich.meteostation.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding


    private var listOfColors = ColorTemplate.VORDIPLOM_COLORS.toList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, null, false).apply {
            lcMain.setTouchEnabled(true)
            lcMain.setScaleEnabled(true)
            lcMain.animateXY(1000, 1000, Easing.EaseInQuad)
        }
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
                            it.data.isSuccess -> setData(it.data.getOrNull()!!)
                            it.data.isFailure -> showError()
                        }
                    }
                }
            }.launchIn(lifecycleScope)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWeather(
            WeatherType.AIR_TEMPERATURE,
            WeatherParameters(2001, WeatherInterval.MONTH, Date(1647451012087L))
        )
    }

    private fun showError() {
        TODO("Not yet implemented")
    }

    private fun showProgressBar() {

    }

    private fun setData(data: Map<String, Map<Date, Float>>) {
        setupXAxis(data.getFirstEntry().value.keys.toList())
        val dataSets = mutableListOf<ILineDataSet>()
        for ((label, XY) in data) {
            dataSets.add(
                LineDataSet(XY.values.mapIndexed { index, value ->
                    Entry(
                        index.toFloat(),
                        value
                    )
                }, label).apply {
                    val color = listOfColors.random()
                    this.color = color
                    listOfColors = listOfColors.filterNot { it == color }
                }
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
        with(binding.lcMain.xAxis) {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            valueFormatter = formatter
        }
    }


}