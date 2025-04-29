package com.example.proect23.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.proect23.R
import com.example.proect23.data.model.Enterprise
import com.example.proect23.databinding.FragmentEnterprisesBinding
import com.example.proect23.ui.enterprises.EnterpriseState
import com.example.proect23.ui.enterprises.EnterpriseViewModel
import com.example.proect23.ui.enterprises.EnterprisesAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.appcompat.widget.SearchView

class EnterprisesFragment : Fragment() {

    private var _binding: FragmentEnterprisesBinding? = null
    private val binding get() = _binding!!
    private val vm: EnterpriseViewModel by viewModels()

    private val adapter = EnterprisesAdapter { enterprise ->
        findNavController().navigate(
            R.id.action_enterprises_to_detail,
            bundleOf("enterpriseId" to enterprise.id)
        )
    }

    private var allEnterprises: List<Enterprise> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterprisesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvEnterprises.adapter = adapter

        // Кнопка добавления предприятия
        binding.fabAddEnterprise.setOnClickListener {
            findNavController().navigate(R.id.action_enterprisesFragment_to_createEnterpriseFragment)
        }

        // Поиск
        initSearchView()

        // Обновление при свайпе
        binding.swipeRefresh.setOnRefreshListener {
            vm.fetchAll()
        }

        // Подписка на состояние
        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collectLatest { state ->
                binding.swipeRefresh.isRefreshing = state is EnterpriseState.Loading
                when (state) {
                    is EnterpriseState.Success -> {
                        allEnterprises = state.list
                        filterAndShow(binding.searchView.query?.toString().orEmpty())
                    }
                    is EnterpriseState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun initSearchView() = with(binding.searchView) {
        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterAndShow(newText.orEmpty())
                return true
            }
        })

        setOnCloseListener {
            filterAndShow("")
            false
        }
    }

    private fun filterAndShow(query: String) {
        val filtered = if (query.isBlank()) {
            allEnterprises
        } else {
            allEnterprises.filter {
                it.name.contains(query.trim(), ignoreCase = true)
            }
        }
        adapter.submitList(filtered)
        binding.tvEmpty.isVisible = filtered.isEmpty() && !binding.swipeRefresh.isRefreshing
    }

    override fun onResume() {
        super.onResume()
        if (vm.shouldRefresh) {
            vm.fetchAll()
            vm.shouldRefresh = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
