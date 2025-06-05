package com.dewipuspitasari0020.laventry.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewipuspitasari0020.laventry.network.KategoriApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KategoriViewModelApi: ViewModel() {
    init {
        retriveData()
    }

    private fun retriveData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = KategoriApi.service.getKategori()
                Log.d("KategoriViewModelApi", "Success: $result")
            } catch (e: Exception){
                Log.d("KategoriViewModelApi", "Failure: ${e.message}")
            }
        }
    }
}