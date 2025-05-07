package com.dewipuspitasari0020.laventry.util

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dewipuspitasari0020.laventry.database.AppDatabase
import com.dewipuspitasari0020.laventry.ui.screen.MainViewModel
import com.dewipuspitasari0020.laventry.viewModel.BarangViewModel
import com.dewipuspitasari0020.laventry.viewModel.KategoriViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val barangDao = AppDatabase.getInstance(context).barangDao
        val kategoriDao = AppDatabase.getInstance(context).kategoriDao
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(barangDao) as T
        } else if (modelClass.isAssignableFrom(BarangViewModel::class.java)){
            return BarangViewModel(barangDao, kategoriDao) as T
        }  else if (modelClass.isAssignableFrom(KategoriViewModel::class.java)) {
            return KategoriViewModel(kategoriDao, barangDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

fun saveImageToInternalStorage(context: Context, imageUri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val fileName = "img_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        file.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

