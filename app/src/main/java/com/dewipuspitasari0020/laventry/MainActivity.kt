package com.dewipuspitasari0020.laventry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dewipuspitasari0020.laventry.ui.screen.MainScreen
import com.dewipuspitasari0020.laventry.ui.theme.LaventryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaventryTheme {
                MainScreen()
            }
        }
    }
}