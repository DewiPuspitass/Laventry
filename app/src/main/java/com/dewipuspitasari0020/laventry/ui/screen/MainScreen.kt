package com.dewipuspitasari0020.laventry.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dewipuspitasari0020.laventry.R
import com.dewipuspitasari0020.laventry.ui.theme.LaventryTheme
import com.dewipuspitasari0020.laventry.ui.theme.bg
import com.dewipuspitasari0020.laventry.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val selectedIndex = remember { mutableStateOf(0) }
    Scaffold(
        containerColor = bg,
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.fotoprofile),
                            contentDescription = "Foto Profil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                },
                actions = {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(color = white)
                            .clickable {  }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah",
                            tint = Color.Black
                        )
                    }
                },
                modifier = modifier.padding(
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
            Surface(
                tonalElevation = 3.dp,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = white
            ){
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedIndex.value == 1,
                        onClick = { selectedIndex.value = 1 },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.home),
                                contentDescription = "Beranda",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = { Text("Home") },
                    )
                    NavigationBarItem(
                        selected = selectedIndex.value == 1,
                        onClick = { selectedIndex.value = 1 },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.inventory),
                                contentDescription = "Inventory",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = { Text("Inventory") }
                    )
                    NavigationBarItem(
                        selected = selectedIndex.value == 1,
                        onClick = { selectedIndex.value = 1 },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.settings),
                                contentDescription = "Pengaturan",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = { Text("Pengaturan") }
                    )
                }
            }
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}


@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 0.dp
            )
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        )  {
            items(3) {
                CardInfo()
            }
        }
        Row (
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column {
                Text("Barang Terbaru", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Column {
                Text("View All")
            }
        }
        LazyColumn (
            verticalArrangement = Arrangement.spacedBy(16.dp),
        )  {
            items(5) {
                CardBarang()
            }
        }
    }

}

@Composable
fun CardInfo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(
                color = white,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = bg, shape = RoundedCornerShape(16.dp))
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.boxopenfull),
                    contentDescription = "Icon dari Flaticon",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "60",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    "out of stock",
                    color = Color.Black
                )
            }
        }
    }
}
@Composable
fun CardBarang() {
    Column(
        modifier = Modifier
            .background(
                color = white,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(color = bg, shape = RoundedCornerShape(16.dp))
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.boxopenfull),
                    contentDescription = "Icon dari Flaticon",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text("Coca Cola", fontWeight = FontWeight.Bold)
                Text("Stock: 10")
                Text("kode: 4237423746")
            }

            Column {
                Text("Rp 5.000")
            }
        }
    }
}


@Preview
@Composable
private fun MainPreview() {
    LaventryTheme{
        MainScreen()
    }
}