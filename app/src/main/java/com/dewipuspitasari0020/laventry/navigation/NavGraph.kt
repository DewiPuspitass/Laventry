package com.dewipuspitasari0020.laventry.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dewipuspitasari0020.laventry.ui.screen.AddItemsScreen
import com.dewipuspitasari0020.laventry.ui.screen.MainScreen

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
    }
}