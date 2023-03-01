package com.paraskcd.unitedwalls.components

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.unitedwalls.ui.theme.BillionDreams
import com.paraskcd.unitedwalls.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

@Composable
fun TopBar(
    screenActive: Int,
    openDrawer: (Boolean) -> Unit,
    openScreen: (Int) -> Unit
) {
    BackHandler(enabled = screenActive != 0) {
        if (screenActive == 4) {
            openScreen(1)
        } else {
            openScreen(0)
        }
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(if (screenActive == 0 || screenActive == 1 || screenActive == 4) 150.dp else 80.dp)
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
                Text(
                    text = "United Walls",
                    fontFamily = BillionDreams,
                    style = TextStyle(
                        fontSize = 48.sp,
                        shadow = Shadow(blurRadius = 10f, offset = Offset(5.0f, 10.0f), color = Color(0x80000000))
                    ),
                    modifier = Modifier.clickable {
                        openScreen(0)
                    }
                )
                Box(modifier = Modifier.width(48.dp))
            }
        }
    }
}