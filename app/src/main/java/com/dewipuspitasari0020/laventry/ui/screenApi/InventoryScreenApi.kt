package com.dewipuspitasari0020.laventry.ui.screenApi

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dewipuspitasari0020.laventry.R
import com.dewipuspitasari0020.laventry.model.Barang
import com.dewipuspitasari0020.laventry.model.User
import com.dewipuspitasari0020.laventry.navigation.Screen
import com.dewipuspitasari0020.laventry.network.ApiStatus
import com.dewipuspitasari0020.laventry.network.BarangApi
import com.dewipuspitasari0020.laventry.network.UserDataStore
import com.dewipuspitasari0020.laventry.ui.screen.BottomBar
import com.dewipuspitasari0020.laventry.ui.theme.bg
import com.dewipuspitasari0020.laventry.ui.theme.white
import com.dewipuspitasari0020.laventry.util.SettingsDataStore
import com.dewipuspitasari0020.laventry.viewModel.BarangViewModelApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreenApi(navController: NavHostController) {
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedIndex = when (currentRoute) {
        Screen.Home.route -> 0
        Screen.Inventory.route -> 1
        Screen.Kategori.route -> 2
        Screen.Settings.route -> 3
        else -> -1
    }

    val viewModel: BarangViewModelApi = viewModel()
    val data by viewModel.data.collectAsState(initial = emptyList())

    val context = LocalContext.current
    val datastore = UserDataStore(context)
    val user by datastore.userFlow.collectAsState(User())

    val status by viewModel.status.collectAsState()

    LaunchedEffect(user.email) {
        viewModel.retriveData(user.email)
    }

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
                            .clickable {
                                if (user.email.isEmpty()){
                                    CoroutineScope(Dispatchers.IO).launch {
                                        signIn(context, datastore)
                                    }
                                } else {
                                    navController.navigate(Screen.Profile.route)
                                    Log.d("SIGN-IN", "User: $user")
                                }

                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (user.photoUrl.isNotEmpty()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Foto Profil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.broken_image)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.account_circle),
                                contentDescription = "Foto Profil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                },
                actions = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(color = white)
                                .clickable { navController.navigate(Screen.TambahBarang.route) }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Tambah",
                                tint = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(color = white)
                                .clickable { CoroutineScope(Dispatchers.IO).launch {
                                    dataStore.saveLayout(!showList)
                                } }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (showList) R.drawable.baseline_grid_view_24
                                    else R.drawable.baseline_view_list_24
                                ),
                                contentDescription = stringResource(
                                    if (showList) R.string.grid
                                    else R.string.list
                                )
                            )
                        }
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
        }
    ) { innerPadding ->
        when (status) {
            ApiStatus.LOADING -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            ApiStatus.SUCCESS -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    if (data.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(R.string.list_kosong))
                        }
                    } else {
                        if (showList) {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(data) { barang ->
                                    val imagePath = BarangApi.getGambarUrl(barang.foto_barang)

                                    CardBarang(
                                        label = barang.nama_barang,
                                        stock = barang.jumlah,
                                        barcode = barang.barcode,
                                        harga = barang.harga,
                                        image = imagePath,
                                        onClick = {
                                            navController.navigate(Screen.EditBarang.withId(barang.id))
                                        }
                                    )
                                }
                            }
                        } else {
                            LazyVerticalStaggeredGrid(
                                modifier = Modifier.fillMaxSize(),
                                columns = StaggeredGridCells.Fixed(2),
                                verticalItemSpacing = 8.dp,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
                            ) {
                                items(data) {
                                    GridItemApi(barang = it) {
                                        navController.navigate(Screen.EditBarang.withId(it.id))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            ApiStatus.FAILED -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(id = R.string.error))
                    Button(
                        onClick = { viewModel.retriveData(user.email) },
                        modifier = Modifier.padding(top = 16.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        Text(text = stringResource(R.string.try_again))
                    }
                }
            }
        }
    }
}

@Composable
fun GridItemApi(barang: Barang, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = white),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = bg),
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(BarangApi.getGambarUrl(barang.foto_barang))
                        .crossfade(true)
                        .build()
                )
                Image(
                    painter = painter,
                    contentDescription = "Gambar Barang",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = barang.nama_barang,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Barcode: ${barang.barcode}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Harga: Rp ${barang.harga}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Stok: ${barang.jumlah}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFF4CAF50), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}