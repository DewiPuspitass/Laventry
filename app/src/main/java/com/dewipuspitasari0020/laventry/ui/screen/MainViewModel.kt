package com.dewipuspitasari0020.laventry.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewipuspitasari0020.laventry.database.BarangDao
import com.dewipuspitasari0020.laventry.model.Barang
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(dao: BarangDao): ViewModel() {
    val data: StateFlow<List<Barang>> = dao.getBarang().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
}