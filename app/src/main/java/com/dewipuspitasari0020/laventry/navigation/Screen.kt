package com.dewipuspitasari0020.laventry.navigation

import com.dewipuspitasari0020.laventry.ui.screen.KEY_ID_BARANG

sealed class Screen (val route: String){
    data object Home: Screen("mainScreen")
    data object TambahBarang: Screen("tambahBarangScreen")
    data object EditBarang: Screen("detailScreen/{$KEY_ID_BARANG}"){
        fun withId(id: Long) = "detailScreen/$id"
    }
}