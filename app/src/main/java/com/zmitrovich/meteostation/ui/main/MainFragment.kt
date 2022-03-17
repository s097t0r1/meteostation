package com.zmitrovich.meteostation.ui.main

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
import com.zmitrovich.meteostation.data.model.lineColors
import com.zmitrovich.meteostation.data.model.parameters.MeteoData
import com.zmitrovich.meteostation.data.model.parameters.MeteoInterval
import com.zmitrovich.meteostation.data.model.parameters.MeteoParameters
import com.zmitrovich.meteostation.databinding.MainFragmentBinding
import com.zmitrovich.meteostation.ui.makeGone
import com.zmitrovich.meteostation.ui.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()

        const val TAG = "com.zmitrovich.meteostation.ui.main.MainFragment"
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding

    private var listOfColors = ColorTemplate.VORDIPLOM_COLORS.toList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false).apply {
            lcMain.setTouchEnabled(true)
            lcMain.setScaleEnabled(true)
            lcMain.animateXY(1000, 1000, Easing.EaseInQuad)
        }
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {

        viewModel.isLoading.onEach { isLoading ->
            if (isLoading) showProgressBar()
        }.launchIn(lifecycleScope)

        viewModel.meteoData.onEach {
            it?.let { setData(it) }
        }.launchIn(lifecycleScope)

        viewModel.error.onEach { isError ->
            if (isError) showError()
        }.launchIn(lifecycleScope)


    }

    private fun showError() {
        binding.lcMain.makeGone()
        binding.pbMain.makeGone()
        binding.tvError.makeVisible()
    }

    private fun showProgressBar() {
        binding.lcMain.makeGone()
        binding.tvError.makeGone()
        binding.pbMain.makeVisible()
    }

    private fun showData() {
        binding.lcMain.makeVisible()
        binding.tvError.makeGone()
        binding.pbMain.makeGone()
    }

    private fun setData(data: MeteoData) {
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
        binding.lcMain.data = LineData(dataSets).apply {
            lineColors = listOfColors
        }
        showData()
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

    fun showBottomSheetDialog() {
        MainBottomSheetFragment.create(object : MainBottomSheetDialogListener {
            override fun onPropertiesSelected(
                weatherType: WeatherType,
                meteoParameters: MeteoParameters
            ) {
                viewModel.getWeather(weatherType, meteoParameters)
            }
        }).show(childFragmentManager, MainBottomSheetFragment.TAG)
    }

}