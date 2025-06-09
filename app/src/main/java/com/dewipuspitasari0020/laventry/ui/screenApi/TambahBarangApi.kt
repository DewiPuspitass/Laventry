package com.dewipuspitasari0020.laventry.ui.screenApi

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import com.dewipuspitasari0020.laventry.ui.screen.DisplayAlertDialog
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.dewipuspitasari0020.laventry.R
import com.dewipuspitasari0020.laventry.model.Kategori
import com.dewipuspitasari0020.laventry.network.BarangApi.getGambarUrl
import com.dewipuspitasari0020.laventry.ui.theme.LaventryTheme
import com.dewipuspitasari0020.laventry.ui.theme.bg
import com.dewipuspitasari0020.laventry.util.saveImageToInternalStorage
import com.dewipuspitasari0020.laventry.viewModel.BarangViewModelApi
import com.dewipuspitasari0020.laventry.viewModel.KategoriViewModelApi
import java.net.HttpURLConnection
import java.net.URL

const val KEY_ID_BARANG = "id"

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemsScreen2(navController: NavHostController, id: Long? = null) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val viewModel: BarangViewModelApi = viewModel()

    Scaffold(
        containerColor = bg,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if(id == null)
                        Text(stringResource(R.string.add_items))
                    else
                        Text(stringResource(R.string.edit_items))
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
                actions = {
                    if (id != null){
                        DeleteAction {
                            showDialog = true
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
        }
    ) { innerPadding ->
        AddItems(modifier = Modifier.padding(innerPadding), id = id, navController = navController)
        if(id != null && showDialog){
            DisplayAlertDialog(
                onDismissRequest = { showDialog = false }) {
                showDialog = false
                viewModel.deleteBarang(id)
                navController.popBackStack()
                Log.d("Delete", "Berhasil di hapus")
            }
        } else {
            Log.e("Delete", "Delete gagal")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AddItems(modifier: Modifier = Modifier, id: Long? = null, navController: NavHostController) {
    val context = LocalContext.current

    val viewModel: BarangViewModelApi = viewModel()
    val viewModel1: KategoriViewModelApi = viewModel()
    val kategoriList by viewModel1.data.collectAsState(initial = emptyList())

    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var savedPath by remember { mutableStateOf("") }
    var imageError by remember { mutableStateOf("") }

    var namaBarang by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var harga by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }

    var namaBarangError by remember { mutableStateOf("") }
    var jumlahError by remember { mutableStateOf("") }
    var hargaError by remember { mutableStateOf("") }
    var barcodeError by remember { mutableStateOf("") }
    var deskripsiError by remember { mutableStateOf("") }
    var selectedCategoryError by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel1.retrieveData()
    }

    LaunchedEffect(id) {
        if (id != null) {
            val barang = viewModel.retrieveBarangById(id)
            barang?.let {
                namaBarang = it.nama_barang
                jumlah = it.jumlah.toString()
                harga = it.harga.toString()
                selectedCategoryId = it.kategoriId
                barcode = it.barcode
                deskripsi = it.deskripsi
                savedPath = it.foto_barang
                imageUri = Uri.parse(getGambarUrl(it.foto_barang))
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            imageError = ""
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
                AsyncImage(
                    model = imageUri.toString(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.broken_image)
                )
            } else {
                DottedBorderBox(modifier = Modifier.fillMaxSize(), onClick = {
                    launcher.launch("image/*")
                })
            }
        }
        if (imageError.isNotBlank()) {
            Text(
                text = imageError,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                InputPendek(
                    label = stringResource(R.string.item_name),
                    placeholder = stringResource(R.string.item_name),
                    value = namaBarang,
                    onValueChange = { namaBarang = it },
                    keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
                )
                if (namaBarangError.isNotBlank()) {
                    Text(text = namaBarangError, color = Color.Red, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 24.dp))
                }

                InputPendek(
                    label = stringResource(R.string.price),
                    placeholder = "Rp 0",
                    value = harga,
                    onValueChange = { harga = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                if (hargaError.isNotBlank()) {
                    Text(text = hargaError, color = Color.Red, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                InputPendek(
                    label = stringResource(R.string.quantity),
                    placeholder = "Quantity",
                    value = jumlah,
                    onValueChange = { jumlah = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                if (jumlahError.isNotBlank()) {
                    Text(text = jumlahError, color = Color.Red, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp))
                }

                DropdownCategory(
                    selectedCategoryId = selectedCategoryId,
                    onCategorySelected = {
                        selectedCategoryId = it
                        Log.d("KategoriTerpilih", "Kategori ID: $it")
                        println("Kategori ID yang dipilih: $it")
                    },
                    kategoriList = kategoriList
                )

                if (selectedCategoryError.isNotBlank()) {
                    Text(text = selectedCategoryError, color = Color.Red, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp))
                }
            }
        }
        InputPendek(
            label = stringResource(R.string.barcode),
            placeholder = "Barcode",
            value = barcode,
            onValueChange = { if (it.length <= 13 && it.all { char -> char.isDigit() }) {
                barcode = it
            } },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        if (barcodeError.isNotBlank()) {
            Text(text = barcodeError, color = Color.Red, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp))
        }

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
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            textStyle = TextStyle(color = Color.Black),
            singleLine = false,
            maxLines = 5
        )

        if (deskripsiError.isNotBlank()) {
            Text(text = deskripsiError, color = Color.Red, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    namaBarangError = ""
                    jumlahError = ""
                    hargaError = ""
                    barcodeError = ""
                    deskripsiError = ""
                    selectedCategoryError = ""
                    imageError = ""

                    if (namaBarang.isBlank()) {
                        namaBarangError = "Nama Barang is required"
                    }

                    if (jumlah.isBlank() || jumlah.toIntOrNull() == null) {
                        jumlahError = "Jumlah must be a valid number"
                    }

                    if (harga.isBlank() || harga.toDoubleOrNull() == null) {
                        hargaError = "Harga must be a valid number"
                    }

                    if (selectedCategoryId == null) {
                        selectedCategoryError = "Category must be selected"
                    }

                    if (barcode.isBlank()) {
                        barcodeError = "Barcode is required"
                    }

                    if (deskripsi.isBlank()) {
                        deskripsiError = "Deskripsi is required"
                    }

                    if (imageUri == null) {
                        imageError = "Image is required"
                    }

                    if (
                        namaBarangError.isBlank() &&
                        jumlahError.isBlank() &&
                        hargaError.isBlank() &&
                        selectedCategoryError.isBlank() &&
                        barcodeError.isBlank() &&
                        deskripsiError.isBlank() &&
                        imageError.isBlank() &&
                        selectedCategoryId != null
                    ) {
                        val kategoriId: Long = selectedCategoryId!!

                        imageUri?.let {
                            val bitmap = uriToBitmap(context, it)
                            if (bitmap != null) {
                                if (id == null) {
                                    viewModel.insertBarang(
                                        namaBarang = namaBarang,
                                        jumlah = jumlah.toInt(),
                                        harga = harga.toDouble(),
                                        kategoriId = kategoriId,
                                        barcode = barcode,
                                        deskripsi = deskripsi,
                                        fotoBitmap = bitmap
                                    )
                                } else {
                                    viewModel.updateBarang(
                                        id = id,
                                        namaBarang = namaBarang,
                                        jumlah = jumlah.toInt(),
                                        harga = harga.toDouble(),
                                        kategoriId = kategoriId,
                                        barcode = barcode,
                                        deskripsi = deskripsi,
                                        fotoBitmap = bitmap
                                    )
                                }
                                navController.popBackStack()
                            } else {
                                imageError = "Gagal memproses gambar"
                            }
                    } ?: run {
                            Log.e("InputError", "Gambar null")
                        }
                        navController.popBackStack()
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

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        when {
            uri.scheme == "content" || uri.scheme == "file" -> {
                val inputStream = context.contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream)
            }
            uri.scheme == "http" || uri.scheme == "https" -> {
                val url = URL(uri.toString())
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                BitmapFactory.decodeStream(inputStream)
            }
            else -> null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
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
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
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
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = keyboardOptions,
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownCategory(
    selectedCategoryId: Long?,
    onCategorySelected: (Long) -> Unit,
    kategoriList: List<Kategori>
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedKategori = kategoriList.find { it.id == selectedCategoryId }

    Column(modifier = Modifier.padding(bottom = 16.dp)) {
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
                value = selectedKategori?.nama_kategori ?: "",
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
                textStyle = TextStyle(color = Color.Black),
                shape = RoundedCornerShape(50.dp),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                kategoriList.forEach { kategori ->
                    DropdownMenuItem(
                        text = { Text(kategori.nama_kategori) },
                        onClick = {
                            onCategorySelected(kategori.id)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.other),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false}
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.delete))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Preview
@Composable
private fun AddItemsPrev() {
    LaventryTheme {
        AddItemsScreen2(rememberNavController())
    }
}