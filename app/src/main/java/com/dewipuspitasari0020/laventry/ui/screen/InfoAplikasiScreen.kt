package com.dewipuspitasari0020.laventry.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dewipuspitasari0020.laventry.R
import com.dewipuspitasari0020.laventry.ui.theme.LaventryTheme
import com.dewipuspitasari0020.laventry.ui.theme.bg
import com.dewipuspitasari0020.laventry.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoAplikasiScreen(navController: NavHostController) {
    Scaffold(
        containerColor = bg,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .clickable { navController.popBackStack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
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
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        val listItem = listOf(
            "Menambahkan barang baru",
            "Mengupdate barang mulai dari nama, jumlah, harga, dll",
            "Menghapus data barang",
            "Menambahkan kategori baru",
            "Mengupdate nama kategori",
            "Menghapus kategori",
        )

        Column (modifier = Modifier.padding(innerPadding).fillMaxSize().verticalScroll(scrollState)){
            Text("Laventry (Laris Inventory) adalah aplikasi yang dirancang untuk membantu pengelolaan persediaan barang di sebuah toko. Nama \"Laventry\" berasal dari singkatan Laris Inventory, yang disesuaikan dengan nama toko, yaitu Laris.",
                modifier = Modifier.padding(18.dp), textAlign = TextAlign.Justify,
            )
            Text("Aplikasi ini memiliki beberapa fitur diantarnya:",
                textAlign = TextAlign.Justify, modifier = Modifier.padding(horizontal = 18.dp)
            )

            listItem.forEach { item ->
                Row(modifier = Modifier.padding(vertical = 4.dp, horizontal = 18.dp)) {
                    Text("- ", color = Color.Black)
                    Text(item, color = Color.Black)
                }
            }
            Card(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Red),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Notes!", color = white, textAlign = TextAlign.Center)
                        Text("Kategori hanya dapat dihapus jika belum digunakan pada data barang mana pun.",  color = white)
                    }
                }
            }

            Text("Card Out of stock = Menampilkan jumlah barang yang memiliki stok/jumlah 0",
                textAlign = TextAlign.Justify, modifier = Modifier.padding(18.dp))

            Text("Card Low stock = Menampilkan jumlah barang yang memiliki stok/jumlah kurang dari 10",
                textAlign = TextAlign.Justify, modifier = Modifier.padding(horizontal = 18.dp))

            Text("Card Total items = Menampilkan total jumlah dari semua barang yang ada",
                textAlign = TextAlign.Justify, modifier = Modifier.padding(18.dp))
        }
    }
}

@Preview
@Composable
private fun InfoAplikasiPreview() {
    LaventryTheme {
        InfoAplikasiScreen(rememberNavController())
    }
}