package com.dewipuspitasari0020.laventry.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.dewipuspitasari0020.laventry.util.ViewModelFactory
import com.dewipuspitasari0020.laventry.viewModel.KategoriViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedIndex = when (currentRoute) {
        Screen.Home.route -> 0
        Screen.Inventory.route -> 1
        Screen.Kategori.route -> 2
        else -> -1
    }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: KategoriViewModel = viewModel(factory = factory)
    val data by viewModel.allKategori.collectAsState()
    Scaffold(
        containerColor = bg,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Category")
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
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = blue,
                onClick = {
                    showDialog = true
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
            if(data.isEmpty()){
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text("Tidak ada kategori saat ini")
                }
            }else{
                LazyColumn{
                    items(data) { kategori ->
                        CardCategory(kategori = kategori)
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
                            Toast.makeText(context, "Kategori tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun CardCategory(kategori: Kategori) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: KategoriViewModel = viewModel(factory = factory)
    var showDialogEdit by remember { mutableStateOf(false) }
    var showDialogDelete by remember { mutableStateOf(false) }


    if (showDialogEdit) {
        DisplayEditCategory(
            kategori = kategori,
            onDismissRequest = { showDialogEdit = false },
            onConfirmation = { updatedKategori ->
                if (updatedKategori.nama_kategori.isNotBlank()) {
                    viewModel.update(
                        id = updatedKategori.id,
                        namaKategori = updatedKategori.nama_kategori
                    )
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
                viewModel.isKategoriUsed(kategori.id) { isUsed ->
                    if (isUsed) {
                        Toast.makeText(context, "Kategori sedang digunakan, tidak bisa dihapus", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.delete(kategori.id)
                        Toast.makeText(context, "Kategori dihapus", Toast.LENGTH_SHORT).show()
                    }
                    showDialogDelete = false
                }
            }
        )
    }


    Card(
        onClick = {
            showDialogEdit = true
        },
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
        CategoryScreen(rememberNavController())
    }
}