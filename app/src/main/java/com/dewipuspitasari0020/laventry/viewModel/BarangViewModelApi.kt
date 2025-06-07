package com.dewipuspitasari0020.laventry.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewipuspitasari0020.laventry.model.Barang
import com.dewipuspitasari0020.laventry.network.BarangApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

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

    suspend fun retrieveBarangById(id: Long): Barang? {
        return try {
            val response = BarangApi.service.getBarang()
            if (response.status) {
                response.data.find { it.id == id }
            } else null
        } catch (e: Exception) {
            Log.d("BarangViewModelApi", "Failure: ${e.message}")
            null
        }
    }

    fun insertBarang(
        namaBarang: String,
        jumlah: Int,
        harga: Double,
        kategoriId: Long,
        barcode: String,
        deskripsi: String,
        fotoBitmap: Bitmap
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestImage = fotoBitmap.toMultipartImagePart("foto_barang")

                val response = BarangApi.service.insertBarang(
                    namaBarang.toRequestBody("text/plain".toMediaTypeOrNull()),
                    jumlah.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    harga.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    kategoriId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    barcode.toRequestBody("text/plain".toMediaTypeOrNull()),
                    deskripsi.toRequestBody("text/plain".toMediaTypeOrNull()),
                    requestImage
                )

                if (response.isSuccessful) {
                    retriveData()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("InsertBarang", "Error upload: $errorBody")
                    Log.e("InsertBarang", "Insert failed: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("InsertBarang", "Exception: ${e.message}")
            }
        }
    }

    fun updateBarang(
        id: Long,
        namaBarang: String,
        jumlah: Int,
        harga: Double,
        kategoriId: Long,
        barcode: String,
        deskripsi: String,
        fotoBitmap: Bitmap
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestImage = fotoBitmap.toMultipartImagePart("foto_barang")

                val response = BarangApi.service.updateBarang(
                    id = id,
                    namaBarang.toRequestBody("text/plain".toMediaTypeOrNull()),
                    jumlah.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    harga.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    kategoriId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    barcode.toRequestBody("text/plain".toMediaTypeOrNull()),
                    deskripsi.toRequestBody("text/plain".toMediaTypeOrNull()),
                    requestImage
                )

                if (response.isSuccessful) {
                    retriveData()
                    Log.i("UpdateBarang", "Update berhasil!")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UpdateBarang", "Gagal update: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("InsertBarang", "Exception: ${e.message}")
            }
        }
    }

    fun Bitmap.toMultipartImagePart(fieldName: String): MultipartBody.Part {
        val bos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()

        val reqFile = bitmapData.toRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fieldName, "image.jpg", reqFile)
    }

    fun deleteBarang(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                BarangApi.service.deleteBarang(id)
                Log.e("BarangViewModelApi", "Delete success")
                retriveData()
            } catch (e: Exception) {
                Log.e("BarangViewModelApi", "Delete failed: ${e.message}")
            }
        }
    }
}