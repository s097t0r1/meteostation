package com.zmitrovich.meteostation.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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
import com.zmitrovich.meteostation.R
import com.zmitrovich.meteostation.data.model.MeteoData
import com.zmitrovich.meteostation.data.model.getFirstEntry
import com.zmitrovich.meteostation.data.model.lineColors
import com.zmitrovich.meteostation.data.model.parameters.MeteoInterval
import com.zmitrovich.meteostation.data.model.parameters.MeteoParameters
import com.zmitrovich.meteostation.databinding.MainFragmentBinding
import com.zmitrovich.meteostation.ui.main_bottom_sheet.MainBottomSheetDialogListener
import com.zmitrovich.meteostation.ui.main_bottom_sheet.MainBottomSheetFragment
import com.zmitrovich.meteostation.ui.makeGone
import com.zmitrovich.meteostation.ui.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainFragment : Fragment(), MainBottomSheetDialogListener {

    companion object {
        fun newInstance() = MainFragment()

        const val TAG = "com.zmitrovich.meteostation.ui.main.MainFragment"
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding

    private var listOfColors = ColorTemplate.VORDIPLOM_COLORS.toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { state ->
                render(state)
            }
        }
    }

    fun render(viewState: MainViewState) = with(binding) {
        tvInitial.isVisible = viewState is MainViewState.Initial
        llMain.isVisible = viewState is MainViewState.DisplayMeteoData
        tvError.isVisible = viewState is MainViewState.Error
        pbMain.isVisible = viewState is MainViewState.Loading

        if (viewState is MainViewState.DisplayMeteoData) {
            binding.tvMeteoType.text =
                getString(R.string.meteo_type_template, getString(viewState.weatherType.value))
            binding.tvMeteoInterval.text =
                getString(
                    R.string.interval_template,
                    getString(viewState.meteoParameters.interval.value)
                )
            setData(viewState.data)
        } else if (viewState is MainViewState.Error) {

        }
    }

    private fun showInitialScreen() {
        binding.tvInitial.makeVisible()
    }

    private fun showError(throwable: Throwable) {
        binding.pbMain.makeGone()
        binding.tvError.makeVisible()
    }

    private fun showProgressBar() {
        binding.tvError.makeGone()
        binding.llMain.makeGone()
        binding.tvInitial.makeGone()
        binding.pbMain.makeVisible()
    }

    private fun showData(weatherType: WeatherType, interval: MeteoInterval) {
        binding.tvMeteoType.text =
            getString(R.string.meteo_type_template, getString(weatherType.value))
        binding.tvMeteoInterval.text =
            getString(R.string.interval_template, getString(interval.value))
        binding.llMain.makeVisible()
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
        binding.lcMain.invalidate()
    }

    private fun setupXAxis(datesList: List<Date>) {
        val formatter: ValueFormatter = object : ValueFormatter() {

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

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
        MainBottomSheetFragment.create()
            .show(childFragmentManager, MainBottomSheetFragment.TAG)
    }

    override fun onPropertiesSelected(weatherType: WeatherType, meteoParameters: MeteoParameters) {
        viewModel.getWeather(weatherType, meteoParameters)
    }

}