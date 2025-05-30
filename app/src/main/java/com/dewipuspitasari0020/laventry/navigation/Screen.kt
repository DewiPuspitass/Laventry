package com.dewipuspitasari0020.laventry.navigation

import com.dewipuspitasari0020.laventry.ui.screen.KEY_ID_BARANG

sealed class Screen (val route: String){
    data object Home: Screen("mainScreen")
    data object TambahBarang: Screen("tambahBarangScreen")
    data object EditBarang: Screen("editScreen/{$KEY_ID_BARANG}"){
        fun withId(id: Long) = "editScreen/$id"
    }
    data object Inventory: Screen("inventoryScreen")
    data object Profile: Screen("profileScreen")
    data object Kategori: Screen("KategoriScreen")
    data object TambahKategory: Screen("TambahKategoriScreen")
    data object Settings: Screen("SettingsScreen")
    data object InfoAplikasi: Screen("InfoAplikasiScreen")
    data object SplashScreen: Screen("splashScreen")

}