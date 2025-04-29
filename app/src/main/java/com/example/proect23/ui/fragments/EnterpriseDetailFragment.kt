package com.example.proect23.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.proect23.data.model.Enterprise
import com.example.proect23.databinding.FragmentEnterpriseDetailBinding
import com.example.proect23.ui.enterprises.EnterpriseDetailState
import com.example.proect23.ui.enterprises.EnterpriseDetailViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EnterpriseDetailFragment : Fragment() {

    private var _binding: FragmentEnterpriseDetailBinding? = null
    private val binding get() = _binding!!

    private val args: EnterpriseDetailFragmentArgs by navArgs()
    private val vm: EnterpriseDetailViewModel by viewModels()

    /** true — режим редактирования */
    private var editable = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = FragmentEnterpriseDetailBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Подписка на состояние
        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collectLatest { state ->
                val loading = state is EnterpriseDetailState.Loading

                binding.progressCenter.isVisible = loading
                binding.btnEdit.isEnabled = !loading && !editable
                binding.btnSave.isEnabled = !loading && editable
                binding.btnDelete.isEnabled = !loading

                when (state) {
                    is EnterpriseDetailState.Success -> {
                        populate(state.enterprise)
                        if (editable) {
                            Snackbar.make(binding.root, "Изменения сохранены", Snackbar.LENGTH_SHORT).show()
                            switchToEdit(false)
                        }
                    }
                    is EnterpriseDetailState.Deleted -> findNavController().popBackStack()
                    is EnterpriseDetailState.Error -> Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    else -> {}
                }
            }
        }

        // Маска для телефона
        binding.etPhone.addTextChangedListener(object : TextWatcher {
            private var editing = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!editable || editing || s == null) return
                editing = true

                val digits = s.filter { it.isDigit() }.toString()

                val formatted = buildString {
                    if (digits.startsWith("8")) {
                        append("+7 ")
                        appendFormatted(digits.drop(1))
                    } else if (digits.startsWith("7")) {
                        append("+7 ")
                        appendFormatted(digits.drop(1))
                    } else if (digits.startsWith("9")) {
                        append("+7 ")
                        appendFormatted(digits)
                    } else {
                        appendFormatted(digits)
                    }
                }

                s.replace(0, s.length, formatted)
                binding.etPhone.setSelection(formatted.length.coerceAtMost(binding.etPhone.text?.length ?: 0))

                editing = false
            }

            private fun StringBuilder.appendFormatted(digits: String) {
                for (i in digits.indices) {
                    when (i) {
                        0, 3, 6 -> append(" ")
                    }
                    append(digits[i])
                }
            }
        })

        // Загрузка данных
        vm.fetchById(args.enterpriseId)

        // Кнопка Изменить
        binding.btnEdit.setOnClickListener {
            switchToEdit(true)
        }

        // Кнопка Сохранить
        binding.btnSave.setOnClickListener {
            binding.tilName.error = null
            binding.tilPhone.error = null

            val name = binding.etName.text.toString().trim()
            val requisites = binding.etRequisites.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val contactPerson = binding.etContactPerson.text.toString().trim()

            // Проверка номера
            val phoneDigits = phone.filter { it.isDigit() }

            if (name.isEmpty()) {
                binding.tilName.error = "Название не может быть пустым"
                return@setOnClickListener
            }
            if (phoneDigits.length != 11) {
                binding.tilPhone.error = "Номер должен содержать 11 цифр"
                return@setOnClickListener
            }

            val updated = Enterprise(
                id = args.enterpriseId,
                name = name,
                requisites = requisites,
                phone = phone,
                contact_person = contactPerson
            )
            vm.update(updated)
        }

        // Кнопка Удалить
        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Удалить предприятие?")
                .setMessage("Действительно удалить это предприятие?")
                .setPositiveButton("Удалить") { _, _ -> vm.delete(args.enterpriseId) }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    private fun switchToEdit(enable: Boolean) {
        editable = enable
        listOf(
            binding.etName,
            binding.etRequisites,
            binding.etPhone,
            binding.etContactPerson
        ).forEach { it.isEnabled = enable }

        if (enable) {
            binding.tilName.error = null
            binding.tilPhone.error = null
        }

        binding.btnEdit.isVisible = !enable
        binding.btnSave.isVisible = enable
        binding.btnSave.isEnabled = enable
    }

    private fun populate(e: Enterprise) {
        binding.etName.setText(e.name)
        binding.etRequisites.setText(e.requisites)
        binding.etPhone.setText(e.phone)
        binding.etContactPerson.setText(e.contact_person)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
