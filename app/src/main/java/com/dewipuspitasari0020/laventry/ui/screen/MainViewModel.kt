package com.dewipuspitasari0020.laventry.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewipuspitasari0020.laventry.network.KategoriApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//class MainViewModel(dao: BarangDao): ViewModel() {
//    val data: StateFlow<List<Barang>> = dao.getBarang().stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(),
//        initialValue = emptyList()
//    )
//}

class MainViewModel: ViewModel() {
    init {
        retrieveData()
    }

    private fun retrieveData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = KategoriApi.service.getKategori()
                Log.d("MainViewModel", "Success: $result.data")
            } catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }
}