package com.dewipuspitasari0020.laventry.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewipuspitasari0020.laventry.model.Barang
import com.dewipuspitasari0020.laventry.network.BarangApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BarangViewModelApi: ViewModel() {
    private val _data = MutableStateFlow<List<Barang>>(emptyList())
    val data: StateFlow<List<Barang>> = _data

    init {
        retriveData()
    }

    fun retriveData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = BarangApi.service.getBarang()
                if (response.status) {
                    _data.value = response.data
                } else {
                    _data.value = emptyList()
                }
            } catch (e: Exception){
                Log.d("BarangViewModelApi", "Failure: ${e.message}")
            }
        }
    }
}