package com.example.proect23.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.proect23.databinding.FragmentCurrenciesBinding
import com.example.proect23.ui.currencies.CurrenciesAdapter
import com.example.proect23.ui.currencies.CurrencyState
import com.example.proect23.ui.currencies.CurrencyViewModel
import kotlinx.coroutines.launch

class CurrenciesFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!

    private val vm: CurrencyViewModel by viewModels()
    private val adapter = CurrenciesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrenciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Устанавливаем адаптер
        binding.rvCurrencies.adapter = adapter

        // Pull-to-refresh
        binding.swipeRefreshCurrencies.setOnRefreshListener { vm.fetchAll() }

        // Подписываемся на состояние
        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collect { state ->
                binding.swipeRefreshCurrencies.isRefreshing = state is CurrencyState.Loading
                when (state) {
                    is CurrencyState.Success -> adapter.submit(state.list)
                    is CurrencyState.Error   ->
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    else -> { /* ничего */ }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
