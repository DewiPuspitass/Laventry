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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dewipuspitasari0020.laventry.R
import com.dewipuspitasari0020.laventry.model.User
import com.dewipuspitasari0020.laventry.network.UserDataStore
import com.dewipuspitasari0020.laventry.ui.theme.LaventryTheme
import com.dewipuspitasari0020.laventry.ui.theme.bg
import com.dewipuspitasari0020.laventry.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController,) {
    val context = LocalContext.current
    val datastore = UserDataStore(context)
    val user by datastore.userFlow.collectAsState(User())
    Scaffold(
        containerColor = bg,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.profile), textAlign = TextAlign.Center)
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = bg,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
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
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(bg)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.photoUrl)
                    .crossfade(true)
                    .build(),
                modifier = Modifier
                    .size(132.dp)
                    .clip(CircleShape),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Row (
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 18.dp)
                    .fillMaxWidth()
                    .background(white, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(16.dp)),
            ){
                Column (
                    modifier = Modifier.padding(16.dp)
                ){
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = stringResource(R.string.icon_person)
                    )
                }
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = user.name)
                }
            }
            Row (
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .background(white, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(16.dp)),
            ){
                Column (
                    modifier = Modifier.padding(16.dp)
                ){
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = stringResource(R.string.icon_info)
                    )
                }
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = user.email)
                }
            }
//            Row (
//                modifier = Modifier
//                    .padding(horizontal = 16.dp)
//                    .fillMaxWidth()
//                    .background(white, shape = RoundedCornerShape(8.dp))
//                    .clip(RoundedCornerShape(16.dp)),
//            ){
//                Column (
//                    modifier = Modifier.padding(16.dp)
//                ){
//                    Icon(
//                        imageVector = Icons.Outlined.LocationOn,
//                        contentDescription = stringResource(R.string.icon_class)
//                    )
//                }
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(text = stringResource(R.string.developer_class))
//                }
//            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewProfileScreen() {
    LaventryTheme {
        ProfileScreen(rememberNavController())
    }
}