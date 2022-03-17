package com.zmitrovich.meteostation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.zmitrovich.meteostation.R
import com.zmitrovich.meteostation.data.model.parameters.MeteoInterval
import com.zmitrovich.meteostation.data.model.parameters.MeteoParameters
import com.zmitrovich.meteostation.databinding.MainBottomSheetFragmentBinding
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

class MainBottomSheetFragment : BottomSheetDialogFragment() {

    var listener: MainBottomSheetDialogListener? = null
    lateinit var binding: MainBottomSheetFragmentBinding

    private val startDateState = MutableStateFlow("")
    private val samplePeriodState = MutableStateFlow("")
    private val weatherTypeState = MutableStateFlow("")

    private val formIsValid = combine(startDateState, samplePeriodState, weatherTypeState) { arr ->
        return@combine arr.fold(true) { acc, next -> acc && next.isNotEmpty() }
    }

    companion object {

        const val TAG = "com.zmitrovich.meteostation.ui.main.MainBottomSheetFragment"

        fun create(listener: MainBottomSheetDialogListener): MainBottomSheetFragment {
            return MainBottomSheetFragment().apply {
                this.listener = listener
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainBottomSheetFragmentBinding.inflate(inflater, container, false)
        setupListeners()
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            formIsValid.collect { isValid -> binding.btnSelect.isEnabled = isValid }
        }
    }

    private fun setupListeners() {
        // Setup start date field
        binding.etStartDate.setOnClickListener { showDatePicker() }

        // Setup sample period dropdown menu
        val samplePeriodList = MeteoInterval.values().toList()
        binding.actvSamplePeriod.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.sample_period_list_item,
                samplePeriodList.map { it.value }
            )
        )

        binding.actvSamplePeriod.setOnItemClickListener { adapterView, view, i, l ->
            samplePeriodState.value = samplePeriodList[i].value
        }

        // Setup weather type dropdown
        val weatherTypeList = WeatherType.values().toList()
        binding.actvWeatherType.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.sample_period_list_item,
                weatherTypeList.map { it.value }
            )
        )

        binding.actvWeatherType.setOnItemClickListener { adapterView, view, i, l ->
            weatherTypeState.value = weatherTypeList[i].value
        }

        // Setup button listener
        binding.btnSelect.setOnClickListener {
            listener?.onPropertiesSelected(
                WeatherType.values().find { weatherTypeState.value == it.value }!!,
                MeteoParameters(
                    MeteoInterval.values().find { samplePeriodState.value == it.value }!!,
                    SimpleDateFormat("EEE, d MMM yyyy", Locale.ROOT).parse(startDateState.value)
                )
            )
            dismiss()
        }

    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.start_date_input_hint))
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val startDate = SimpleDateFormat("EEE, d MMM yyyy", Locale.ROOT).format(Date(it))
            binding.etStartDate.setText(
                startDate
            )
            startDateState.value = startDate

        }
        datePicker.show(childFragmentManager, null)
    }

}