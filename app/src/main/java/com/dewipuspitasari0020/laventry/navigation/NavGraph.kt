package com.dewipuspitasari0020.laventry.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dewipuspitasari0020.laventry.ui.screen.AddItemsScreen
import com.dewipuspitasari0020.laventry.ui.screen.CategoryScreen
import com.dewipuspitasari0020.laventry.ui.screen.InventoryScreen
import com.dewipuspitasari0020.laventry.ui.screen.KEY_ID_BARANG
import com.dewipuspitasari0020.laventry.ui.screen.MainScreen
import com.dewipuspitasari0020.laventry.ui.screen.ProfileScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(route = Screen.Home.route){
            MainScreen(navController)
        }
        composable(route = Screen.TambahBarang.route) {
            AddItemsScreen(navController)
        }
        composable(
            route = Screen.EditBarang.route,
            arguments = listOf(
                navArgument(KEY_ID_BARANG) { type = NavType.LongType }
            )
        ){ navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_BARANG)
            AddItemsScreen(navController, id)
        }
        composable(route = Screen.Inventory.route) {
            InventoryScreen(navController)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(route = Screen.Category.route) {
            CategoryScreen(navController)
        }
        composable(route = Screen.TambahKategory.route) {
            CategoryScreen(navController)
        }
    }
}