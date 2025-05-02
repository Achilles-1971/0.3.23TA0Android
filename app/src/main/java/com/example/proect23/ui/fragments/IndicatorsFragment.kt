package com.example.proect23.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.proect23.databinding.FragmentIndicatorsBinding
import com.example.proect23.ui.indicators.IndicatorState
import com.example.proect23.ui.indicators.IndicatorViewModel
import com.example.proect23.ui.indicators.IndicatorsAdapter
import kotlinx.coroutines.launch

class IndicatorsFragment : Fragment() {

    private var _binding: FragmentIndicatorsBinding? = null
    private val binding get() = _binding!!

    private val vm: IndicatorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndicatorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = IndicatorsAdapter { indicator ->
            val action = IndicatorsFragmentDirections
                .actionIndicatorsFragmentToIndicatorValuesFragment(
                    indicatorId = indicator.id,
                    indicatorName = indicator.name
                )
            findNavController().navigate(action)
        }
        binding.rvIndicators.adapter = adapter

        binding.fabAnalysis.setOnClickListener {
            val action = IndicatorsFragmentDirections
                .actionIndicatorsFragmentToWeightedIndicatorsFragment(enterpriseId = 1)
            findNavController().navigate(action)
        }

        binding.swipeRefreshIndicators.setOnRefreshListener { vm.fetchAll() }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collect { state ->
                binding.swipeRefreshIndicators.isRefreshing = state is IndicatorState.Loading
                when (state) {
                    is IndicatorState.Success -> adapter.submit(state.list)
                    is IndicatorState.Error -> Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_LONG
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
