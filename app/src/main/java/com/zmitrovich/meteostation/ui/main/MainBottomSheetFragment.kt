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

    private val startDateState = MutableStateFlow<String?>(null)
    private val samplePeriodState = MutableStateFlow<MeteoInterval?>(null)
    private val weatherTypeState = MutableStateFlow<WeatherType?>(null)

    private val formIsValid = combine(startDateState, samplePeriodState, weatherTypeState) { arr ->
        arr.fold(true) { acc, value -> value != null && acc}
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
        val intervalList = MeteoInterval.values()
        binding.actvSamplePeriod.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.sample_period_list_item,
                intervalList.map { getString(it.value) }
            )
        )

        binding.actvSamplePeriod.setOnItemClickListener { _, _, i, _ ->
            samplePeriodState.value = intervalList[i]
        }

        // Setup weather type dropdown menu
        val weatherTypeList = WeatherType.values()
        binding.actvWeatherType.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.sample_period_list_item,
                weatherTypeList.map { getString(it.value) }
            )
        )

        binding.actvWeatherType.setOnItemClickListener { _, _, i, _ ->
            weatherTypeState.value = weatherTypeList[i]
        }

        // Setup button listener
        binding.btnSelect.setOnClickListener {
            listener?.onPropertiesSelected(
                weatherTypeState.value!!,
                MeteoParameters(
                    samplePeriodState.value!!,
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
            binding.etStartDate.setText(startDate)
            startDateState.value = startDate
        }
        datePicker.show(childFragmentManager, null)
    }

}