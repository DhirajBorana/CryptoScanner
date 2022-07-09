package com.example.cryptoscanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel() {

    private val _scannedAddress = MutableSharedFlow<String>()
    val scannedAddress = _scannedAddress.asSharedFlow()

    suspend fun setScannedAddress(address: String) {
        viewModelScope.launch {
            _scannedAddress.emit(address)
        }
    }
}