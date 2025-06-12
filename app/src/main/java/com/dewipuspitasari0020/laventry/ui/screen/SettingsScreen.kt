package com.dewipuspitasari0020.laventry.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dewipuspitasari0020.laventry.model.User
import com.dewipuspitasari0020.laventry.navigation.Screen
import com.dewipuspitasari0020.laventry.network.UserDataStore
import com.dewipuspitasari0020.laventry.ui.theme.LaventryTheme
import com.dewipuspitasari0020.laventry.ui.theme.bg
import com.dewipuspitasari0020.laventry.ui.theme.blue
import com.dewipuspitasari0020.laventry.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedIndex = when (currentRoute) {
        Screen.Home.route -> 0
        Screen.Inventory.route -> 1
        Screen.Kategori.route -> 2
        Screen.Settings.route -> 3
        else -> -1
    }

    val context = LocalContext.current
    val datastore = UserDataStore(context)
    val user by datastore.userFlow.collectAsState(User())

    val isLoggedIn = user.email.isNotEmpty()

    Scaffold(
        containerColor = bg,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Settings")
                },
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 0.dp
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bg,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomBar(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(Screen.Home.route)
                        1 -> navController.navigate(Screen.Inventory.route)
                        2 -> navController.navigate(Screen.Kategori.route)
                        3 -> navController.navigate(Screen.Settings.route)
                    }
                }
            )
        },
    ){ innerPadding ->
        ScreenContentSettings(modifier = Modifier.padding(innerPadding), navController = navController, isLoggedIn = isLoggedIn)
    }
}

@Composable
fun ScreenContentSettings(modifier: Modifier = Modifier, navController: NavHostController, isLoggedIn: Boolean) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (isLoggedIn) {
            CardInfo(
                onClick = {
                    navController.navigate(Screen.Profile.route)
                },
                label = "Profile"
            )
        }
        CardInfo(
            onClick = {
                navController.navigate(Screen.InfoAplikasi.route)
            },
            label = "Info Aplikasi"
        )
    }
}

@Composable
fun CardInfo(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(blue),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(label, color = white)
            }
            Row(Modifier.padding(16.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Edit",
                    tint = white
                )
            }
        }
    }
}


@Preview
@Composable
private fun SettingPreview() {
    LaventryTheme {
        SettingsScreen(rememberNavController())
    }
}