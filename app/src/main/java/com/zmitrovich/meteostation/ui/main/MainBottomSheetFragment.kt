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

    private val startDateField = FormField.create<String>(
        "",
        { it.isNotEmpty() }
    )
    private val samplePeriodField = FormField.create<MeteoInterval?>(
        null,
        { it != null }
    )
    private val weatherTypeField = FormField.create<WeatherType?>(
        null,
        { it != null }
    )

    private val formIsValid = combine(startDateField.state, samplePeriodField.state, weatherTypeField.state) { arr ->
        arr.fold(true) { acc, value -> value && acc}
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
            samplePeriodField.value = intervalList[i]
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
            weatherTypeField.value = weatherTypeList[i]
        }

        // Setup button listener
        binding.btnSelect.setOnClickListener {
            listener?.onPropertiesSelected(
                weatherTypeField.value!!,
                MeteoParameters(
                    samplePeriodField.value!!,
                    SimpleDateFormat("EEE, d MMM yyyy", Locale.ROOT).parse(startDateField.value)
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
            startDateField.value = startDate
        }
        datePicker.show(childFragmentManager, null)
    }

}