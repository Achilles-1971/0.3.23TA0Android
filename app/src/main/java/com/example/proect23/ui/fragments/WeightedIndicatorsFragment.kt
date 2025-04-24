package com.example.proect23.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.proect23.databinding.FragmentWeightedIndicatorsBinding
import com.example.proect23.ui.weighted_indicators.WeightedIndicatorState
import com.example.proect23.ui.weighted_indicators.WeightedIndicatorViewModel
import com.example.proect23.ui.weighted_indicators.WeightedIndicatorsAdapter
import kotlinx.coroutines.launch

class WeightedIndicatorsFragment : Fragment() {

    private var _binding: FragmentWeightedIndicatorsBinding? = null
    private val binding get() = _binding!!

    private val vm: WeightedIndicatorViewModel by viewModels()
    private val adapter = WeightedIndicatorsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeightedIndicatorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvWeighted.adapter = adapter
        binding.swipeRefreshWeighted.setOnRefreshListener {
            vm.fetchAll(enterpriseId = 1)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collect { state ->
                binding.swipeRefreshWeighted.isRefreshing = state is WeightedIndicatorState.Loading
                when (state) {
                    is WeightedIndicatorState.Loading -> { /* крутилка */ }
                    is WeightedIndicatorState.Success -> adapter.submit(state.list)
                    is WeightedIndicatorState.Error   ->
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
