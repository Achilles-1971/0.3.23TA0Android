package com.example.proect23.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.proect23.databinding.FragmentExchangeRatesBinding
import com.example.proect23.ui.exchange_rates.ExchangeRateState
import com.example.proect23.ui.exchange_rates.ExchangeRateViewModel
import com.example.proect23.ui.exchange_rates.ExchangeRatesAdapter
import kotlinx.coroutines.launch

class ExchangeRatesFragment : Fragment() {

    private var _binding: FragmentExchangeRatesBinding? = null
    private val binding get() = _binding!!

    private val vm: ExchangeRateViewModel by viewModels()
    private val adapter = ExchangeRatesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExchangeRatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvExchangeRates.adapter = adapter

        binding.swipeRefreshExchange.setOnRefreshListener {
            vm.fetchAll()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collect { state ->
                binding.swipeRefreshExchange.isRefreshing = state is ExchangeRateState.Loading

                when (state) {
                    is ExchangeRateState.Loading -> {
                    }
                    is ExchangeRateState.Success -> {
                        adapter.submit(state.list)
                    }
                    is ExchangeRateState.Error -> {
                        Toast
                            .makeText(requireContext(), state.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
