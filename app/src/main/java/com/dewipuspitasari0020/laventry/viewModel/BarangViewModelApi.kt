package com.dewipuspitasari0020.laventry.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewipuspitasari0020.laventry.model.Barang
import com.dewipuspitasari0020.laventry.model.BarangStats
import com.dewipuspitasari0020.laventry.model.BarangUiState
import com.dewipuspitasari0020.laventry.network.ApiStatus
import com.dewipuspitasari0020.laventry.network.BarangApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
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

    var dashboardStats by mutableStateOf<BarangStats?>(null)
        private set

    var pesanError by mutableStateOf<String?>(null)
        private set

    private val _uiState = MutableStateFlow(BarangUiState(status = ApiStatus.SUCCESS, data = emptyList()))

    val uiState: StateFlow<BarangUiState> = _uiState.asStateFlow()

    fun clearDashboardData() {
        dashboardStats = null
        pesanError = null
    }

    fun muatStatistikDashboard(userId: String) {
        viewModelScope.launch {
            try {
                val response = BarangApi.service.getBarangStats(userId)
                if (response.success) {
                    dashboardStats = response.data
                    pesanError = null
                } else {
                    pesanError = response.message ?: "Terjadi kesalahan."
                }
            } catch (e: Exception) {
                pesanError = "Gagal terhubung ke server: ${e.message}"
            }
        }
    }

    fun retriveData(userId: String) {
        viewModelScope.launch {
            _uiState.value = BarangUiState(status = ApiStatus.LOADING)
            try {
                val result = BarangApi.service.getBarang(userId)
                _uiState.value = BarangUiState(status = ApiStatus.SUCCESS, data = result.data)

            } catch (e: Exception) {
                _uiState.value = BarangUiState(status = ApiStatus.FAILED, error = e.message)
            }
        }
    }

    suspend fun retrieveBarangById(userId: String, id: Long): Barang? {
        return try {
            val response = BarangApi.service.getBarangById(userId, id)
            if (response.status) {
                response.data
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
            status.value = ApiStatus.LOADING
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
                    status.value = ApiStatus.SUCCESS
                } else {
                    if (response.code() == 413){
                        errorMessage.value = "File Image terlalu besar."
                    } else {
                        val errorBody = response.errorBody()?.string()
                        errorMessage.value = "Gagal menyimpan barang. ${response.message()}"
                        Log.e("InsertBarang", "Error upload: $errorBody")
                        status.value = ApiStatus.FAILED
                    }
                }
            } catch (e: Exception) {
                Log.e("InsertBarang", "Exception: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun updateBarang(
        userId: String,
        id: Long,
        namaBarang: String,
        jumlah: Int,
        harga: Double,
        kategoriId: Long,
        barcode: String,
        deskripsi: String,
        fotoBitmap: Bitmap?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val namaBody = namaBarang.toRequestBody("text/plain".toMediaType())
                val jumlahBody = jumlah.toString().toRequestBody("text/plain".toMediaType())
                val hargaBody = harga.toString().toRequestBody("text/plain".toMediaType())
                val kategoriBody = kategoriId.toString().toRequestBody("text/plain".toMediaType())
                val barcodeBody = barcode.toRequestBody("text/plain".toMediaType())
                val deskripsiBody = deskripsi.toRequestBody("text/plain".toMediaType())

                val requestImage = fotoBitmap?.toMultipartImagePart("foto_barang")

                Log.d("UpdateBarang", "Nama: $namaBarang, Jumlah: $jumlah, Harga: $harga, Kategori: $kategoriId, Barcode: $barcode, Deskripsi: $deskripsi")
                val call = BarangApi.service.updateBarang(
                    userId,
                    id,
                    namaBody,
                    jumlahBody,
                    hargaBody,
                    kategoriBody,
                    barcodeBody,
                    deskripsiBody,
                    requestImage
                )

                val response = call.execute()

                if (response.isSuccessful) {
                    retriveData(userId)
                    Log.i("UpdateBarang", "Update berhasil!")
                    status.value = ApiStatus.SUCCESS

                } else {
                    if (response.code() == 413){
                        errorMessage.value = "File Image terlalu besar."
                    } else {
                        val errorBody = response.errorBody()?.string()
                        errorMessage.value = "Gagal update barang. ${response.message()}"
                        Log.e("UpdateBarang", "Error upload: $errorBody")
                        status.value = ApiStatus.FAILED
                    }
                }
            } catch (e: Exception) {
                Log.e("InsertBarang", "Exception: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    private fun Bitmap.toMultipartImagePart(fieldName: String): MultipartBody.Part {
        val bos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val reqFile = bos.toByteArray()
            .toRequestBody("image/jpeg".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(fieldName, "image.jpg", reqFile)
    }

    fun deleteBarang(userId: String, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val response = BarangApi.service.deleteBarang(userId, id)

                if (response.isSuccessful) {
                    Log.i("BarangViewModelApi", "Delete success for id: $id")
                    retriveData(userId)
                    status.value = ApiStatus.SUCCESS
                } else {
                    Log.e("BarangViewModelApi", "Delete failed on server: ${response.message()}")
                    errorMessage.value = "Gagal menghapus barang."
                    status.value = ApiStatus.FAILED
                }
            } catch (e: Exception) {
                Log.e("BarangViewModelApi", "Delete exception: ${e.message}")
                errorMessage.value = "Terjadi kesalahan: ${e.message}"
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun onLogout() {
        clearDashboardData()
        _uiState.value = BarangUiState(status = ApiStatus.SUCCESS, data = emptyList())
    }
}