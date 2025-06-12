package com.dewipuspitasari0020.laventry.ui.screenApi

import android.content.Context
import android.util.Log
import com.dewipuspitasari0020.laventry.ui.screen.BottomBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dewipuspitasari0020.laventry.BuildConfig
import com.dewipuspitasari0020.laventry.R
import com.dewipuspitasari0020.laventry.model.User
import com.dewipuspitasari0020.laventry.navigation.Screen
import com.dewipuspitasari0020.laventry.network.ApiStatus
import com.dewipuspitasari0020.laventry.network.BarangApi
import com.dewipuspitasari0020.laventry.network.UserDataStore
import com.dewipuspitasari0020.laventry.ui.theme.LaventryTheme
import com.dewipuspitasari0020.laventry.ui.theme.bg
import com.dewipuspitasari0020.laventry.ui.theme.white
import com.dewipuspitasari0020.laventry.viewModel.BarangViewModelApi
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenApi(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current
    val datastore = UserDataStore(context)
    val user by datastore.userFlow.collectAsState(User())

    val selectedIndex = when (currentRoute) {
        Screen.Home.route -> 0
        Screen.Inventory.route -> 1
        Screen.Kategori.route -> 2
        Screen.Settings.route -> 3
        else -> -1
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
        ScreenContent(Modifier.padding(innerPadding), user.email, navController = navController)
    }
}


@Composable
fun ScreenContent(modifier: Modifier = Modifier, userId: String, navController: NavHostController) {
    val viewModel:BarangViewModelApi = viewModel()
    val data by viewModel.data.collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()
    val status by viewModel.status.collectAsState()

    LaunchedEffect(userId) {
        viewModel.retriveData(userId)
    }

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
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            item {
                CardInfo(stringResource(R.string.out_of_stock), "11k", painterResource(R.drawable.boxopenfull))
            }
            item {
                CardInfo(stringResource(R.string.lowstock), "11", painterResource(R.drawable.chart))
            }
            item {
                CardInfo(stringResource(R.string.total_items), "11", painterResource(R.drawable.items))
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
        when (uiState.status) {
            ApiStatus.LOADING -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
            ApiStatus.SUCCESS -> {
                if (uiState.data.isEmpty()) {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(R.string.list_kosong))
                    }
                }else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.data) { barang ->
                            val imagePath = BarangApi.getGambarUrl(barang.foto_barang)

                            CardBarang(
                                label = barang.nama_barang,
                                stock = barang.jumlah,
                                barcode = barang.barcode,
                                harga = barang.harga,
                                image = imagePath,
                                onClick = {
                                    navController.navigate(Screen.EditBarang.withId(barang.id))
                                    Log.d("MainScreen", "Berhasil masuk ke edit")
                                }
                            )
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
                        onClick = { viewModel.retriveData(userId) },
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
fun CardInfo(label: String, jumlah: String, gambar: Painter, modifier: Modifier = Modifier) {
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
                    painter = gambar,
                    contentDescription = "Icon dari drawable",
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
                    jumlah,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    label,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun CardBarang(
    label: String,
    stock: Int,
    barcode: String,
    harga: Double,
    image: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = white),
//        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = bg),
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image)
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

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Barcode: $barcode",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Box(
                    modifier = Modifier
                        .background(Color(0xFF4CAF50), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Stok: $stock",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }

            Text(
                text = "Rp ${harga.toInt()}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Bottom)
            )
        }
    }
}

suspend fun signIn(context: Context, dataStore: UserDataStore) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException){
        Log.e("SIGN-IN", "Error: ${e.message}")
    }
}

private suspend fun handleSignIn(result: GetCredentialResponse, dataStore: UserDataStore) {
    val credential = result.credential
    if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama, email, photoUrl))
        } catch (e: GoogleIdTokenParsingException){
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    }else{
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}

@Preview
@Composable
private fun MainPreview() {
    LaventryTheme{
        MainScreenApi(rememberNavController())
    }
}