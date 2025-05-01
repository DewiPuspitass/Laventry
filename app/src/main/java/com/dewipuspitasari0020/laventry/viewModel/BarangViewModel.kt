package com.dewipuspitasari0020.laventry.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewipuspitasari0020.laventry.database.BarangDao
import com.dewipuspitasari0020.laventry.model.Barang
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BarangViewModel (private val dao: BarangDao): ViewModel() {
    private val _outOfStock = MutableStateFlow(0)
    val outOfStock: StateFlow<Int> = _outOfStock

    private val _lowStock = MutableStateFlow(0)
    val lowStock: StateFlow<Int> = _lowStock

    private val _totalItems = MutableStateFlow(0)
    val totalItems: StateFlow<Int> = _totalItems

    val allBarang: StateFlow<List<Barang>> = dao.getBarang()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun insert(namaBarang: String, jumlah: Int, harga: Double, kategori: String, barcode: String, deskripsi: String, fotoBarang: String){
        val barang = Barang(
            nama_barang = namaBarang,
            jumlah = jumlah,
            harga = harga,
            kategori = kategori,
            barcode = barcode,
            deskripsi = deskripsi,
            foto_barang = fotoBarang
        )
        viewModelScope.launch(Dispatchers.IO){
            dao.insert(barang)
        }
    }

    suspend fun getBarang(id: Long): Barang? {
        return dao.getBarangById(id)
    }

    fun update(
        id: Long,
        namaBarang: String,
        jumlah: Int,
        harga: Double,
        kategori: String,
        barcode: String,
        deskripsi: String,
        fotoBarang: String
    ) {
        viewModelScope.launch {
            val barang = Barang(
                id = id,
                nama_barang = namaBarang,
                jumlah = jumlah,
                harga = harga,
                kategori = kategori,
                barcode = barcode,
                deskripsi = deskripsi,
                foto_barang = fotoBarang
            )
            dao.update(barang)
        }
    }


    fun delete(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }

    fun loadSummaryData() {
        viewModelScope.launch(Dispatchers.IO) {
            _outOfStock.value = dao.getOutOfStockCount()
            _lowStock.value = dao.getLowStockCount()
            _totalItems.value = dao.getTotalItemCount() ?: 0
        }
    }
}