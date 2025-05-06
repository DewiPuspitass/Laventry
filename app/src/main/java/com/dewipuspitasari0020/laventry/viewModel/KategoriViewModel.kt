package com.dewipuspitasari0020.laventry.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewipuspitasari0020.laventry.database.KategoriDao
import com.dewipuspitasari0020.laventry.model.Kategori
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KategoriViewModel(private val dao: KategoriDao): ViewModel() {
    val allKategori: StateFlow<List<Kategori>> = dao.getKategori()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun insert(namaKategori: String){
        val kategori = Kategori(
            nama_kategori = namaKategori
        )
        viewModelScope.launch(Dispatchers.IO){
            dao.insert(kategori)
        }
    }

    suspend fun getKategori(id: Long): Kategori? {
        return dao.getKategoriById(id)
    }

    fun update(
        id: Long,
        namaKategori: String
    ) {
        viewModelScope.launch {
            val kategori = Kategori(
                id = id,
                nama_kategori = namaKategori,
            )
            dao.update(kategori)
        }
    }

    fun delete(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}