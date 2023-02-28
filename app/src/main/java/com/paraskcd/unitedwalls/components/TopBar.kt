package com.paraskcd.unitedwalls.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.unitedwalls.ui.theme.BillionDreams
import com.paraskcd.unitedwalls.viewmodel.CategoryViewModel

@Composable
fun TopBar(
    screenActive: Int,
    openDrawer: (Boolean) -> Unit,
    openScreen: (Int) -> Unit,
    categoryViewModel: CategoryViewModel,
    categoryActive: String?
) {
    val categories = categoryViewModel.categories.observeAsState().value
    val loadingCategories = categoryViewModel.loadingCategories.observeAsState().value
    BackHandler(enabled = screenActive != 0) {
        openScreen(0)
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(if (screenActive == 0 || screenActive == 1) 150.dp else 80.dp)
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
            if (screenActive == 0 || screenActive == 1) {
                LazyRow(modifier = Modifier
                    .padding(bottom = 12.dp)) {
                    item {
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    item {
                        Button(onClick = { openScreen(0) }, colors = ButtonDefaults.buttonColors(containerColor = if (screenActive == 0) MaterialTheme.colorScheme.primary else Color.Transparent), modifier = Modifier.alpha(0.75f)) {
                            Text(text = "All Wallpapers")
                        }
                    }
                    if (loadingCategories == true) {
                        item {
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        item {
                            Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), modifier = Modifier.alpha(0.75f)) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                    categories?.size?.let {
                        item { 
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        items(it) { index ->
                            val category = categories[index]
                            Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = if (screenActive == 1 && category._id == categoryActive) MaterialTheme.colorScheme.primary else Color.Transparent), modifier = Modifier.alpha(0.75f)) {
                                Text(text = category.name)
                            }
                        }
                    }
                }
            }
            Box(modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSecondary))
        }
    }
}