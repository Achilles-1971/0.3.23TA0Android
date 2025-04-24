package com.example.proect23.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.proect23.databinding.FragmentEnterprisesBinding
import com.example.proect23.ui.enterprises.EnterpriseState
import com.example.proect23.ui.enterprises.EnterpriseViewModel
import com.example.proect23.ui.enterprises.EnterprisesAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EnterprisesFragment : Fragment() {
    private var _binding: FragmentEnterprisesBinding? = null
    private val binding get() = _binding!!
    private val vm: EnterpriseViewModel by viewModels()
    private val adapter = EnterprisesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEnterprisesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvEnterprises.adapter = adapter

        // Swipe to refresh
        binding.swipeRefresh.setOnRefreshListener { vm.fetchAll() }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collect { state ->
                binding.swipeRefresh.isRefreshing = state is EnterpriseState.Loading
                when (state) {
                    is EnterpriseState.Success -> adapter.submit(state.list)
                    is EnterpriseState.Error -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
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
