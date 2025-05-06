package com.dewipuspitasari0020.laventry.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dewipuspitasari0020.laventry.R
import com.dewipuspitasari0020.laventry.navigation.Screen
import com.dewipuspitasari0020.laventry.ui.theme.LaventryTheme
import com.dewipuspitasari0020.laventry.ui.theme.bg
import com.dewipuspitasari0020.laventry.ui.theme.blue
import com.dewipuspitasari0020.laventry.ui.theme.white

@Composable
fun CategoryScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedIndex = when (currentRoute) {
        Screen.Home.route -> 0
        Screen.Inventory.route -> 1
        Screen.Category.route -> 2
        else -> -1
    }
    Scaffold(
        containerColor = bg,
        bottomBar = {
            BottomBar(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(Screen.Home.route)
                        1 -> navController.navigate(Screen.Inventory.route)
                        2 -> navController.navigate(Screen.Category.route)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = blue,
                onClick = {
//                    navController.navigate(Screen.FormBaru.route)
                    {}
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_category),
                    tint = white
                )
            }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

            cardCategory()
        }
    }
}

@Composable
fun cardCategory(modifier: Modifier = Modifier) {
    Card(
        onClick = {},
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(blue),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (Modifier.padding(16.dp)){
                Text("Makanan", color = white)
            }
            Column (Modifier.padding(16.dp)){
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Back",
                    tint = white
                )
            }
        }
    }
}
@Preview
@Composable
private fun CategoryPreview() {
    LaventryTheme {
        CategoryScreen(rememberNavController())
    }
}