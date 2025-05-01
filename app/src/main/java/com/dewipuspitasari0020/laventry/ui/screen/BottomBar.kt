package com.dewipuspitasari0020.laventry.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dewipuspitasari0020.laventry.R
import com.dewipuspitasari0020.laventry.ui.theme.blue
import com.dewipuspitasari0020.laventry.ui.theme.white

@Composable
fun BottomBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf("Home", "Inventory", "Category", "Settings")
            val icons = listOf(R.drawable.home, R.drawable.inventory, R.drawable.tags,R.drawable.settings)

            items.forEachIndexed { index, label ->
                val isSelected = index == selectedIndex
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) blue else Color.Transparent)
                        .clickable { onItemSelected(index) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = icons[index]),
                        contentDescription = label,
                        modifier = Modifier.size(24.dp),
                        tint = (if (isSelected) white else Color.Black)
                    )
                    Text(
                        text = label,
                        color = (if (isSelected) white else Color.Black),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
