package com.example.proect23.ui.enterprises

import com.example.proect23.data.model.Enterprise

sealed class EnterpriseState {
    object Loading : EnterpriseState()
    data class Success(val list: List<Enterprise>) : EnterpriseState()
    data class Error(val message: String) : EnterpriseState()
}
