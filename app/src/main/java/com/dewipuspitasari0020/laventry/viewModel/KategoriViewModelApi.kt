package com.dewipuspitasari0020.laventry.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewipuspitasari0020.laventry.model.Kategori
import com.dewipuspitasari0020.laventry.network.ApiStatus
import com.dewipuspitasari0020.laventry.network.KategoriApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KategoriViewModelApi: ViewModel() {
    private val _data = MutableStateFlow<List<Kategori>>(emptyList())
    val data: StateFlow<List<Kategori>> = _data

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun clearErrorMessage() {
        errorMessage.value = null
    }

    init {
        retrieveData()
    }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val response = KategoriApi.service.getKategori()
                if (response.status) {
                    _data.value = response.data
                } else {
                    _data.value = emptyList()
                }
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.e("KategoriViewModelApi", "Failure: ${e.message}")
                _data.value = emptyList()
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun insert(namaKategori: String) {
        viewModelScope.launch {
            try {
                KategoriApi.service.insertKategori(namaKategori)
                retrieveData()
            } catch (e: Exception) {
                Log.e("KategoriViewModelApi", "Insert failed: ${e.message}")
            }
        }
    }

    fun updateKategori(updatedKategori: Kategori) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                KategoriApi.service.updateKategori(
                    id = updatedKategori.id,
                    kategori = mapOf("nama_kategori" to updatedKategori.nama_kategori)
                )
                retrieveData()
            } catch (e: Exception) {
                Log.e("KategoriViewModelApi", "Update failed: ${e.message}")
            }
        }
    }

    fun deleteKategori(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = KategoriApi.service.deleteKategori(id)

                if (response.isSuccessful) {
                    errorMessage.value = "Kategori berhasil dihapus."
                    retrieveData()
                } else {
                    if (response.code() == 409) {
                        errorMessage.value = "Maaf, kategori sedang dipakai oleh barang lain."
                        status.value = ApiStatus.SUCCESS
                    } else {
                        errorMessage.value = "Gagal menghapus. Error: ${response.code()}"
                        status.value = ApiStatus.FAILED
                    }
                }
            } catch (e: Exception) {
                errorMessage.value = "Terjadi kesalahan pada jaringan."
                status.value = ApiStatus.FAILED
            }
        }
    }
}
