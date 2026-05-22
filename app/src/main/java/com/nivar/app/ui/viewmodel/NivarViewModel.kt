package com.nivar.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nivar.app.data.model.Grievance
import com.nivar.app.data.repository.NivarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NivarViewModel @Inject constructor(private val repository: NivarRepository) : ViewModel() {

    private val _grievanceState = MutableStateFlow<GrievanceUiState>(GrievanceUiState.Idle)
    val grievanceState: StateFlow<GrievanceUiState> = _grievanceState.asStateFlow()

    fun submitGrievance(title: String, description: String, lat: Double, long: Double, category: String, imageUrl: String?, isAnonymous: Boolean) {
        viewModelScope.launch {
            _grievanceState.value = GrievanceUiState.Loading
            val grievance = Grievance(
                title = title,
                description = description,
                category = category,
                latitude = lat,
                longitude = long,
                imageUrl = imageUrl,
                isAnonymous = isAnonymous
            )
            val result = repository.createGrievance(grievance)
            if (result.isSuccess) {
                _grievanceState.value = GrievanceUiState.Success
            } else {
                _grievanceState.value = GrievanceUiState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }
}

sealed class GrievanceUiState {
    object Idle : GrievanceUiState()
    object Loading : GrievanceUiState()
    object Success : GrievanceUiState()
    data class Error(val message: String) : GrievanceUiState()
}
