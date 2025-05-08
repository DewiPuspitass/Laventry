package com.dewipuspitasari0020.laventry

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dewipuspitasari0020.laventry.navigation.SetupNavGraph
import com.dewipuspitasari0020.laventry.ui.theme.LaventryTheme
import com.dewipuspitasari0020.laventry.ui.theme.blue_logo
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaventryTheme {
                SetupNavGraph()
            }
        }
    }
}

@Composable
fun SplashScreen1(navController: NavController) {
    LaunchedEffect(key1 = true) {
        delay(3000L)
        navController.navigate("mainScreen") {
            popUpTo("splashScreen") { inclusive = true }
        }
    }

    Column (
        modifier = Modifier.fillMaxSize().background(blue_logo),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.laventry_logo),
            contentDescription = "Logo",
            modifier = Modifier.width(300.dp).height(300.dp)
        )
    }
}

