package com.example.proect23.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.proect23.data.model.Enterprise
import com.example.proect23.databinding.FragmentCreateEnterpriseBinding
import com.example.proect23.ui.enterprises.EnterpriseViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CreateEnterpriseFragment : Fragment() {

    private var _binding: FragmentCreateEnterpriseBinding? = null
    private val binding get() = _binding!!
    private val vm: EnterpriseViewModel by viewModels()

    private companion object {
        const val TOTAL_FIELDS = 4
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCreateEnterpriseBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.progressFields.max = TOTAL_FIELDS

        configureValidation()
        configurePhoneMask()

        binding.btnSave.setOnClickListener { saveEnterprise() }
    }

    private fun configureValidation() {
        val edits = listOf(
            binding.etName,
            binding.etRequisites,
            binding.etPhone,
            binding.etContactPerson
        )
        edits.forEach { it.addTextChangedListener { updateUiByFilledFields() } }
        updateUiByFilledFields()
    }

    private fun updateUiByFilledFields() {
        val filled = listOf(
            binding.etName,
            binding.etRequisites,
            binding.etPhone,
            binding.etContactPerson
        ).count { it.text?.isNotBlank() == true }

        binding.progressFields.progress = filled
        binding.btnSave.isEnabled = filled == TOTAL_FIELDS
    }

    private fun configurePhoneMask() {
        binding.etPhone.addTextChangedListener(object : TextWatcher {
            private var editing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (editing || s == null) return
                editing = true

                val digits = s.filter { it.isDigit() }.toString()
                val formatted = buildString {
                    if (digits.startsWith("8") || digits.startsWith("7") || digits.startsWith("9")) {
                        append("+7 ")
                        appendFormatted(digits.drop(if (digits[0] == '8' || digits[0] == '7') 1 else 0))
                    } else {
                        appendFormatted(digits)
                    }
                }

                s.replace(0, s.length, formatted)
                binding.etPhone.setSelection(formatted.length.coerceAtMost(binding.etPhone.text?.length ?: 0))
                editing = false
            }

            private fun StringBuilder.appendFormatted(d: String) {
                for (i in d.indices) {
                    when (i) {
                        0, 3, 6 -> append(" ")
                    }
                    append(d[i])
                }
            }
        })
    }

    private fun saveEnterprise() {
        val phoneDigits = binding.etPhone.text!!.filter { it.isDigit() }
        if (phoneDigits.isNotEmpty() && phoneDigits.length != 11) {
            Snackbar.make(binding.root, "Телефон должен содержать 11 цифр", Snackbar.LENGTH_LONG).show()
            return
        }

        val enterprise = Enterprise(
            id = 0,
            name           = binding.etName.text!!.trim().toString(),
            requisites     = binding.etRequisites.text!!.trim().toString(),
            phone          = binding.etPhone.text!!.trim().toString(),
            contact_person = binding.etContactPerson.text!!.trim().toString()
        )

        lifecycleScope.launch {
            if (vm.create(enterprise).isSuccess) {
                Snackbar.make(binding.root, "Предприятие создано", Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Snackbar.make(binding.root, "Ошибка создания", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
