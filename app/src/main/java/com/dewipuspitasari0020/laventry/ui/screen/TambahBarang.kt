package com.dewipuspitasari0020.laventry.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.dewipuspitasari0020.laventry.R
import com.dewipuspitasari0020.laventry.ui.theme.LaventryTheme
import com.dewipuspitasari0020.laventry.ui.theme.bg
import com.dewipuspitasari0020.laventry.util.ViewModelFactory
import com.dewipuspitasari0020.laventry.util.saveImageToInternalStorage
import com.dewipuspitasari0020.laventry.viewModel.BarangViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemsScreen(navcontroller: NavHostController) {
    Scaffold(
        containerColor = bg,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.add_items))
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .clickable { navcontroller.popBackStack() },
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
        }
    ) { innerPadding ->
        AddItems(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun AddItems(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: BarangViewModel = viewModel(factory = factory)

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var savedPath by remember { mutableStateOf("") }

    var namaBarang by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            val path = saveImageToInternalStorage(context, it)
            if (path != null) {
                savedPath = path
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                DottedBorderBox(modifier = Modifier.fillMaxSize(), onClick = {
                    launcher.launch("image/*")
                })
            }
        }


//        if (foto.isNotEmpty()) {
//            Image(
//                painter = rememberAsyncImagePainter(model = foto),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(180.dp)
//                    .clip(RoundedCornerShape(12.dp)),
//                contentScale = ContentScale.Crop
//            )
//        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                InputPendek(stringResource(R.string.item_name), stringResource(R.string.item_name), namaBarang) { namaBarang = it }
                InputPendek(stringResource(R.string.price), "Rp 0", harga) { harga = it }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                InputPendek(stringResource(R.string.quantity), stringResource(R.string.quantity), jumlah) { jumlah = it }
                DropdownCategory(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
                }
        }
        InputPendek(stringResource(R.string.barcode), "Barcode", barcode) { barcode = it }
        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        TextField(
            value = deskripsi,
            onValueChange = { deskripsi = it },
            modifier = Modifier.height(120.dp)
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(50.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Gray
                ),
                border = BorderStroke(1.dp, color = bg)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    if (
                        namaBarang.isNotBlank() &&
                        jumlah.isNotBlank() &&
                        harga.isNotBlank() &&
                        selectedCategory.isNotBlank() &&
                        barcode.isNotBlank() &&
                        deskripsi.isNotBlank() &&
                        savedPath.isNotBlank()
                    ) {
                        viewModel.insert(
                            namaBarang = namaBarang,
                            jumlah = jumlah.toInt(),
                            harga = harga.toDouble(),
                            kategori = selectedCategory,
                            barcode = barcode,
                            deskripsi = deskripsi,
                            fotoBarang = savedPath
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6),
                    contentColor = Color.White
                )
            ) {
                Text("Save")
            }

        }
    }
}

@Composable
fun DottedBorderBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .padding(1.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val stroke = Stroke(
                width = 3f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
            drawRoundRect(
                color = Color.Gray,
                style = stroke,
                cornerRadius = CornerRadius(30f, 30f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Upload",
                tint = Color.Gray,
                modifier = Modifier.size(40.dp)
            )
            Text(text = "Upload Images", color = Color.Gray)
        }
    }
}

@Composable
fun InputPendek(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(50.dp)),
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownCategory(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Makanan", "Minuman", "Bahan Nyuci", "Bahan Masak", "Sashetan")

    Column(
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
    Text(
        text = stringResource(R.string.category),
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 6.dp)
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(stringResource(R.string.category), color = Color.Gray) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(50.dp),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onCategorySelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
    }
}


@Preview
@Composable
private fun AddItemsPrev() {
    LaventryTheme {
        AddItemsScreen(rememberNavController())
    }
}
