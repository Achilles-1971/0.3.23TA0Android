package com.example.proect23.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.proect23.databinding.FragmentIndicatorValuesBinding
import com.example.proect23.ui.indicator_values.IndicatorValueState
import com.example.proect23.ui.indicator_values.IndicatorValueViewModel
import com.example.proect23.ui.indicator_values.IndicatorValuesAdapter
import kotlinx.coroutines.launch

class IndicatorValuesFragment : Fragment() {

    private var _binding: FragmentIndicatorValuesBinding? = null
    private val binding get() = _binding!!

    private val vm: IndicatorValueViewModel by viewModels()
    private val adapter = IndicatorValuesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndicatorValuesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvValues.adapter = adapter
        binding.swipeRefreshValues.setOnRefreshListener { vm.fetchAll() }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collect { state ->
                binding.swipeRefreshValues.isRefreshing = state is IndicatorValueState.Loading

                when (state) {
                    is IndicatorValueState.Loading -> { /* только крутилка */ }
                    is IndicatorValueState.Success -> adapter.submit(state.list)
                    is IndicatorValueState.Error   ->
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
