package com.example.proect23.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.proect23.R
import com.example.proect23.databinding.FragmentIndicatorValuesBinding
import com.example.proect23.ui.indicator_values.IndicatorValueState
import com.example.proect23.ui.indicator_values.IndicatorValueViewModel
import com.example.proect23.ui.indicator_values.IndicatorValuesAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class IndicatorValuesFragment : Fragment(R.layout.fragment_indicator_values) {

    private var _binding: FragmentIndicatorValuesBinding? = null
    private val binding get() = _binding!!

    private val args: IndicatorValuesFragmentArgs by navArgs()
    private val vm: IndicatorValueViewModel by viewModels()
    private val adapter = IndicatorValuesAdapter()

    private val sdfDisplay = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val sdfApi     = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentIndicatorValuesBinding.bind(view)

        val indicatorId   = args.indicatorId
        val indicatorName = args.indicatorName

        binding.tvIndicatorValues.text = indicatorName
        requireActivity().title        = indicatorName
        binding.rvValues.adapter        = adapter

        val currencies = listOf("— оригинальная валюта —", "RUB", "USD", "EUR")
        binding.spinnerCurrency.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            currencies
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        binding.spinnerCurrency.setSelection(0)

        fun toIso(dateStr: String): String? =
            if (dateStr.length == 10) runCatching {
                sdfApi.format(sdfDisplay.parse(dateStr)!!)
            }.getOrNull()
            else null

        fun updateFilterRange() {
            val from = binding.editFromDate.text.toString()
            val to   = binding.editToDate.text.toString()
            if (from.length == 10 || to.length == 10) {
                val text = buildString {
                    append("Фильтр: ")
                    if (from.length == 10) append("с $from")
                    if (from.length == 10 && to.length == 10) append(" ")
                    if (to.length   == 10) append("по $to")
                }
                binding.tvFilterRange.text = text
                binding.tvFilterRange.visibility = View.VISIBLE
            } else {
                binding.tvFilterRange.visibility = View.GONE
            }
        }

        fun reload() {
            updateFilterRange()
            binding.tvNoResults.visibility = View.GONE
            vm.fetchAll(
                indicatorId    = indicatorId,
                targetCurrency = currencies.getOrNull(binding.spinnerCurrency.selectedItemPosition)
                    ?.takeIf { it != currencies[0] },
                fromDate = toIso(binding.editFromDate.text.toString()),
                toDate   = toIso(binding.editToDate.text.toString())
            )
        }

        binding.spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>, v: View?, pos: Int, id: Long) = reload()
            override fun onNothingSelected(p: AdapterView<*>) {}
        }

        fun pickDate(target: View) {
            val edit = target as? android.widget.EditText ?: return
            val cal  = Calendar.getInstance()
            DatePickerDialog(requireContext(),
                { _, y, m, d ->
                    cal.set(y, m, d)
                    edit.setText(sdfDisplay.format(cal.time))
                    reload()
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.editFromDate.setOnClickListener { pickDate(it) }
        binding.editToDate  .setOnClickListener { pickDate(it) }

        binding.btnResetFilters.setOnClickListener {
        binding.spinnerCurrency.setSelection(0)
            binding.editFromDate.text?.clear()
            binding.editToDate  .text?.clear()
            reload()
        }

        binding.btnToggleFilters.setOnClickListener {
            val card = binding.cardFilters as CardView
            val hidden = card.visibility == View.GONE
            card.visibility = if (hidden) View.VISIBLE else View.GONE
            binding.btnToggleFilters.setImageResource(
                if (hidden) android.R.drawable.arrow_up_float
                else android.R.drawable.arrow_down_float
            )
        }

        binding.swipeRefreshValues.setOnRefreshListener { reload() }

        reload()

        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collect { state ->
                binding.swipeRefreshValues.isRefreshing = state is IndicatorValueState.Loading
                when (state) {
                    is IndicatorValueState.Success -> {
                        if (state.list.isEmpty()) {
                            binding.tvNoResults.visibility = View.VISIBLE
                        }
                        adapter.submit(state.list)
                    }
                    is IndicatorValueState.Error -> Toast.makeText(
                        requireContext(), state.message, Toast.LENGTH_LONG
                    ).show()
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
