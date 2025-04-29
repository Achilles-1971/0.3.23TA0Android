package com.example.proect23.ui.enterprises

import com.example.proect23.data.model.Enterprise

sealed class EnterpriseDetailState {
    object Loading : EnterpriseDetailState()
    data class Success(val enterprise: Enterprise) : EnterpriseDetailState()
    data class Error(val message: String) : EnterpriseDetailState()
    object Deleted : EnterpriseDetailState()
}
