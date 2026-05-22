package com.nivar.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nivar.app.data.model.Scheme
import com.nivar.app.data.repository.NivarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(private val repository: NivarRepository) : ViewModel() {

    private val _schemes = MutableStateFlow<List<Scheme>>(emptyList())
    val schemes: StateFlow<List<Scheme>> = _schemes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchSchemes()
    }

    fun fetchSchemes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.getSchemes()
                if (result.isSuccess) {
                    _schemes.value = result.getOrDefault(emptyList())
                } else {
                    _error.value = "Failed to fetch schemes: ${result.exceptionOrNull()?.message}"
                }
            } catch (e: Exception) {
                _error.value = "Connection Error: Ensure backend is running at 10.0.2.2:8000"
            }
            _isLoading.value = false
        }
    }
}
