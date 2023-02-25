package com.paraskcd.unitedwalls.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.unitedwalls.ui.theme.BillionDreams

@Composable
fun TopBar(
    openDrawer: (Boolean) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(72.dp)
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(MaterialTheme.colorScheme.primary, Color.Transparent)
            )
        )
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { openDrawer(true) }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu Icon")
                }
                Text(text = "United Walls", fontFamily = BillionDreams, fontSize = 48.sp)
                Box(modifier = Modifier.width(48.dp))
            }
            Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(MaterialTheme.colorScheme.onPrimary))
        }
    }
}