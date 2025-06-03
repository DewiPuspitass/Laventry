package com.dewipuspitasari0020.laventry.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewipuspitasari0020.laventry.model.Barang
import com.dewipuspitasari0020.laventry.model.Kategori
import com.dewipuspitasari0020.laventry.model.KategoriRequest
import com.dewipuspitasari0020.laventry.network.BarangApi
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
    var dataBarang = mutableStateOf(emptyList<Barang>())
        private set

    var dataKategori = mutableStateOf(emptyList<Kategori>())
        private set

    var kategoriSuccess = mutableStateOf(false)
        private set

    var kategoriError = mutableStateOf<String?>(null)
        private set

    init {
        retrieveDataKategori()
        retrieveDataBarang()
    }

    private fun retrieveDataKategori(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataKategori.value = KategoriApi.service.getKategori().data
            } catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

    private fun retrieveDataBarang(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataBarang.value = BarangApi.service.getBarang().data
            } catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

    fun createKategori(namaKategori: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = KategoriApi.service.createKategori(KategoriRequest(namaKategori))
                if (response.status) {
                    kategoriSuccess.value = true
                    kategoriError.value = null
                    retrieveDataKategori()
                }
            } catch (e: Exception) {
                kategoriError.value = e.message
                kategoriSuccess.value = false
            }
        }
    }

    fun updateKategori(id: Long, namaKategori: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = KategoriApi.service.updateKategori(id.toInt(), KategoriRequest(namaKategori))
                if (response.status) {
                    retrieveDataKategori()
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Update Error: ${e.message}")
            }
        }
    }

    fun deleteKategori(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = KategoriApi.service.createKategori(KategoriRequest(id.toString()))
                KategoriApi.service.deleteKategori(id.toInt())
                if (response.status) {
                    retrieveDataKategori()
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Delete Error: ${e.message}")
            }
        }
    }

    fun clearKategoriStatus() {
        kategoriSuccess.value = false
        kategoriError.value = null
    }

}