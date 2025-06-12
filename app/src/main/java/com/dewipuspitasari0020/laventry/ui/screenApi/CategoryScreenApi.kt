package com.dewipuspitasari0020.laventry.ui.screenApi

import com.dewipuspitasari0020.laventry.ui.screen.BottomBar
import com.dewipuspitasari0020.laventry.ui.screen.DisplayAddCategory
import com.dewipuspitasari0020.laventry.ui.screen.DisplayAlertDeleteDialog
import com.dewipuspitasari0020.laventry.ui.screen.DisplayEditCategory
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dewipuspitasari0020.laventry.R
import com.dewipuspitasari0020.laventry.model.Kategori
import com.dewipuspitasari0020.laventry.navigation.Screen
import com.dewipuspitasari0020.laventry.ui.theme.LaventryTheme
import com.dewipuspitasari0020.laventry.ui.theme.bg
import com.dewipuspitasari0020.laventry.ui.theme.blue
import com.dewipuspitasari0020.laventry.ui.theme.white
import com.dewipuspitasari0020.laventry.viewModel.KategoriViewModelApi
import androidx.compose.runtime.collectAsState
import com.dewipuspitasari0020.laventry.network.ApiStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreenApi(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedIndex = when (currentRoute) {
        Screen.Home.route -> 0
        Screen.Inventory.route -> 1
        Screen.Kategori.route -> 2
        Screen.Settings.route -> 3
        else -> -1
    }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val viewModel: KategoriViewModelApi = viewModel()

    val status by viewModel.status.collectAsState()

    val data by viewModel.data.collectAsState(initial = emptyList())
    val errorMsg by viewModel.errorMessage

    LaunchedEffect(errorMsg) {
        errorMsg?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        containerColor = bg,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Category") },
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp),
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
        floatingActionButton = {
            FloatingActionButton(
                containerColor = blue,
                onClick = { showDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_category),
                    tint = white
                )
            }
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
                Column(modifier = Modifier.padding(innerPadding)) {
                    if (data.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Tidak ada kategori saat ini")
                        }
                    } else {
                        LazyColumn {
                            items(data) { kategori ->
                                CardCategory(kategori = kategori, viewModel = viewModel)
                            }
                        }
                    }

                    if (showDialog) {
                        DisplayAddCategory(
                            onDismissRequest = { showDialog = false },
                            onConfirmation = { inputText ->
                                if (inputText.isNotBlank()) {
                                    viewModel.insert(namaKategori = inputText)
                                    showDialog = false
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Kategori tidak boleh kosong",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
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
                        onClick = { viewModel.retrieveData() },
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
fun CardCategory(kategori: Kategori, viewModel: KategoriViewModelApi) {
    val context = LocalContext.current
    var showDialogEdit by remember { mutableStateOf(false) }
    var showDialogDelete by remember { mutableStateOf(false) }

    if (showDialogEdit) {
        DisplayEditCategory(
            kategori = kategori,
            onDismissRequest = { showDialogEdit = false },
            onConfirmation = { updatedKategori ->
                if (updatedKategori.nama_kategori.isNotBlank()) {
                    viewModel.updateKategori(updatedKategori)
                    showDialogEdit = false
                } else {
                    Toast.makeText(context, "Kategori tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    if (showDialogDelete) {
        DisplayAlertDeleteDialog(
            onDismissRequest = { showDialogDelete = false },
            onConfirmation = {
                viewModel.deleteKategori(kategori.id)
                showDialogDelete = false
            }
        )
    }

    Card(
        onClick = { showDialogEdit = true },
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(blue),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(kategori.nama_kategori, color = white)
            }
            Row(Modifier.padding(vertical = 8.dp, horizontal = 2.dp)) {
                IconButton(onClick = { showDialogEdit = true }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = white
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { showDialogDelete = true }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Hapus",
                        tint = white
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CategoryPreview() {
    LaventryTheme {
        CategoryScreenApi(rememberNavController())
    }
}