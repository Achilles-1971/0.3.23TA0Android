package com.example.proect23.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.proect23.R
import com.example.proect23.data.model.Enterprise
import com.example.proect23.data.repository.EnterpriseRepository
import com.example.proect23.databinding.FragmentWeightedIndicatorsBinding
import com.example.proect23.ui.weighted_indicators.*
import kotlinx.coroutines.launch

class WeightedIndicatorsFragment : Fragment(R.layout.fragment_weighted_indicators) {

    private var _binding: FragmentWeightedIndicatorsBinding? = null
    private val binding get() = _binding!!

    private val vm:  WeightedIndicatorViewModel by viewModels()
    private val adapter = WeightedIndicatorsAdapter()
    private val repo    = EnterpriseRepository()

    private var enterprises: List<Enterprise> = emptyList()
    private val currencies = listOf("— оригинальная валюта —", "RUB", "USD", "EUR")

    // ──────── ► теперь на уровне класса ◄ ─────────
    private fun updateFilterText() {
        val ent = enterprises.getOrNull(binding.spinnerEnterprise.selectedItemPosition)?.name
        val cur = currencies[binding.spinnerCurrencyWeighted.selectedItemPosition]
        val parts = mutableListOf<String>()
        ent?.let { parts += it }
        if (cur != currencies[0]) parts += "валюта: $cur"
        binding.tvFilterInfo.text = parts.joinToString("; ")
        binding.tvFilterInfo.visibility = if (parts.size > 1) View.VISIBLE else View.GONE
    }

    private fun reload() {
        if (enterprises.isEmpty()) return         // ещё не подгрузили список
        updateFilterText()
        binding.tvNoWeighted.visibility = View.GONE
        val selectedEnt = enterprises[binding.spinnerEnterprise.selectedItemPosition]
        vm.fetchAll(
            enterpriseId   = selectedEnt.id,
            targetCurrency = currencies[binding.spinnerCurrencyWeighted.selectedItemPosition]
                .takeIf { it != currencies[0] }
        )
    }
    // ──────────────────────────────────────────────

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentWeightedIndicatorsBinding.inflate(inflater, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvWeighted.adapter = adapter

        // статический адаптер валют
        binding.spinnerCurrencyWeighted.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            currencies
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        // загружаем предприятия
        viewLifecycleOwner.lifecycleScope.launch {
            enterprises = repo.getAllSortedSafe()
            binding.spinnerEnterprise.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                enterprises            // Enterprise.toString() = name
            ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

            initListeners()
            reload()                 // ← теперь видит функцию
        }

        // наблюдаем за состоянием ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collect { state ->
                binding.swipeRefreshWeighted.isRefreshing = state is WeightedIndicatorState.Loading
                when (state) {
                    is WeightedIndicatorState.Success -> {
                        if (state.list.isEmpty()) binding.tvNoWeighted.visibility = View.VISIBLE
                        adapter.submit(state.list)
                    }
                    is WeightedIndicatorState.Error ->
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    else -> {}
                }
            }
        }
    }

    private fun initListeners() {
        val selListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>, v: View?, pos: Int, id: Long) = reload()
            override fun onNothingSelected(p: AdapterView<*>) {}
        }
        binding.spinnerEnterprise.onItemSelectedListener       = selListener
        binding.spinnerCurrencyWeighted.onItemSelectedListener = selListener

        binding.btnResetWeighted.setOnClickListener {
            binding.spinnerEnterprise.setSelection(0)
            binding.spinnerCurrencyWeighted.setSelection(0)
            reload()
        }

        binding.btnToggleFiltersWeighted.setOnClickListener {
            val card   = binding.cardFiltersWeighted as CardView
            val hidden = card.visibility == View.GONE
            card.visibility = if (hidden) View.VISIBLE else View.GONE
            binding.btnToggleFiltersWeighted.setImageResource(
                if (hidden) android.R.drawable.arrow_up_float
                else android.R.drawable.arrow_down_float
            )
        }

        binding.swipeRefreshWeighted.setOnRefreshListener { reload() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
