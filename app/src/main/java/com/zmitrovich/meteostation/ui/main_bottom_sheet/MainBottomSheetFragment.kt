package com.zmitrovich.meteostation.ui.main_bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.zmitrovich.meteostation.R
import com.zmitrovich.meteostation.data.model.parameters.MeteoInterval
import com.zmitrovich.meteostation.data.model.parameters.MeteoParameters
import com.zmitrovich.meteostation.databinding.MainBottomSheetFragmentBinding
import com.zmitrovich.meteostation.ui.main.WeatherType
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

class MainBottomSheetFragment : BottomSheetDialogFragment() {

    var listener: MainBottomSheetDialogListener? = null
    lateinit var binding: MainBottomSheetFragmentBinding

    val viewModel: MainBottomSheetViewModel by viewModels()

    companion object {

        const val TAG = "com.zmitrovich.meteostation.ui.main_bottom_sheet.MainBottomSheetFragment"

        fun create(): MainBottomSheetFragment {
            return MainBottomSheetFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = (parentFragment as? MainBottomSheetDialogListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
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
            viewModel.formIsValid.collect { isValid -> binding.btnSelect.isEnabled = isValid }
        }
    }

    private fun setupListeners() {
        // Setup start date field
        binding.etStartDate.setOnClickListener { showDatePicker() }
        viewModel.startDateField.setAfterValidationListener { isValid ->
            if (!isValid) binding.etStartDate.setError("Error")
        }

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
            viewModel.samplePeriodField.value = intervalList[i]
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
            viewModel.weatherTypeField.value = weatherTypeList[i]
        }

        // Setup button listener
        binding.btnSelect.setOnClickListener {
            listener?.onPropertiesSelected(
                viewModel.weatherTypeField.value!!,
                MeteoParameters(
                    viewModel.samplePeriodField.value!!,
                    SimpleDateFormat("EEE, d MMM yyyy", Locale.ROOT).parse(viewModel.startDateField.value)
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
            viewModel.startDateField.value = startDate
        }
        datePicker.show(childFragmentManager, null)
    }

}