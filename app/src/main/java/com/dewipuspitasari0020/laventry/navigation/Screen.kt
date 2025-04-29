package com.dewipuspitasari0020.laventry.navigation

sealed class Screen (val route: String){
    data object Home: Screen("mainScreen")
    data object TambahBarang: Screen("tambahBarangScreen")
}