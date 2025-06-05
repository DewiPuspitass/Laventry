package com.dewipuspitasari0020.laventry.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewipuspitasari0020.laventry.network.BarangApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BarangViewModelApi: ViewModel() {
    init {
        retriveData()
    }

    private fun retriveData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = BarangApi.service.getBarang()
                Log.d("BarangViewModelApi", "Success: $result")
            } catch (e: Exception){
                Log.d("BarangViewModelApi", "Failure: ${e.message}")
            }
        }
    }
}