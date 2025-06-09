package com.dewipuspitasari0020.laventry.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dewipuspitasari0020.laventry.SplashScreen1
import com.dewipuspitasari0020.laventry.model.User
import com.dewipuspitasari0020.laventry.ui.screen.AddItemsScreen
import com.dewipuspitasari0020.laventry.ui.screen.InfoAplikasiScreen
import com.dewipuspitasari0020.laventry.ui.screen.InventoryScreen
import com.dewipuspitasari0020.laventry.ui.screen.KEY_ID_BARANG
import com.dewipuspitasari0020.laventry.ui.screen.ProfileScreen
import com.dewipuspitasari0020.laventry.ui.screen.SettingsScreen
import com.dewipuspitasari0020.laventry.ui.screenApi.AddItemsScreen2
import com.dewipuspitasari0020.laventry.ui.screenApi.CategoryScreenApi
import com.dewipuspitasari0020.laventry.ui.screenApi.InventoryScreenApi
import com.dewipuspitasari0020.laventry.ui.screenApi.MainScreenApi

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ){
        composable(route = Screen.SplashScreen.route) {
            SplashScreen1(navController = navController)
        }
        composable(route = Screen.Home.route){
            MainScreenApi(navController)
        }
        composable(route = Screen.TambahBarang.route) {
            AddItemsScreen2(navController)
        }
        composable(
            route = Screen.EditBarang.route,
            arguments = listOf(
                navArgument(KEY_ID_BARANG) { type = NavType.LongType }
            )
        ){ navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_BARANG)
            AddItemsScreen2(navController, id)
        }
        composable(route = Screen.Inventory.route) {
            InventoryScreenApi(navController)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(route = Screen.Kategori.route) {
            CategoryScreenApi(navController)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(route = Screen.InfoAplikasi.route) {
            InfoAplikasiScreen(navController)
        }
    }
}