package com.dewipuspitasari0020.laventry.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dewipuspitasari0020.laventry.database.BarangDb
import com.dewipuspitasari0020.laventry.ui.screen.MainViewModel

class ViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = BarangDb.getInstance(context).dao
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}