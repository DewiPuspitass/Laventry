package com.dewipuspitasari0020.laventry.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewipuspitasari0020.laventry.model.Barang
import com.dewipuspitasari0020.laventry.network.ApiStatus
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

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun clearErrorMessage() {
        errorMessage.value = null
    }

    fun retriveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val response = BarangApi.service.getBarang(userId)
                if (response.status) {
                    _data.value = response.data
                } else {
                    _data.value = emptyList()
                }
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception){
                Log.d("BarangViewModelApi", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    suspend fun retrieveBarangById(id: Long, userId: String): Barang? {
        return try {
            val response = BarangApi.service.getBarang(userId)
            if (response.status) {
                response.data.find { it.id == id }
            } else null
        } catch (e: Exception) {
            Log.d("BarangViewModelApi", "Failure: ${e.message}")
            null
        }
    }

    fun insertBarang(
        userId: String,
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
                    userId,
                    namaBarang.toRequestBody("text/plain".toMediaTypeOrNull()),
                    jumlah.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    harga.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    kategoriId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    barcode.toRequestBody("text/plain".toMediaTypeOrNull()),
                    deskripsi.toRequestBody("text/plain".toMediaTypeOrNull()),
                    requestImage
                )

                if (response.isSuccessful) {
                    retriveData(userId)
                } else {
                    if (response.code() == 413){
                        errorMessage.value = "File Image terlalu besar."
                    } else {
                        val errorBody = response.errorBody()?.string()
                        errorMessage.value = "Gagal menyimpan barang. ${response.message()}"
                        Log.e("InsertBarang", "Error upload: $errorBody")
                    }
                }
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.e("InsertBarang", "Exception: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
                status.value = ApiStatus.FAILED
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
//                    retriveData(userId)
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
//                retriveData(userId)
            } catch (e: Exception) {
                Log.e("BarangViewModelApi", "Delete failed: ${e.message}")
            }
        }
    }
}