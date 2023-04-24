package com.paraskcd.unitedwalls.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.paraskcd.unitedwalls.BuildConfig
import com.paraskcd.unitedwalls.components.Screen
import com.paraskcd.unitedwalls.components.WallpaperImage
import com.paraskcd.unitedwalls.model.Category
import com.paraskcd.unitedwalls.utils.Constants
import com.paraskcd.unitedwalls.viewmodel.WallsViewModel

@Composable
fun CategoryScreen(
    openDrawer: (Boolean) -> Unit,
    isDrawerActive: Boolean,
    screenActive: Int,
    category: Category?,
    loadingCategory: Boolean?,
    makeCategoryWallScreenActive: (flag: Boolean, index: Int) -> Unit
) {
    Screen(
        openDrawer = openDrawer,
        isDrawerActive = isDrawerActive,
        screenActive = screenActive,
        screenIndex = 4
    ) {
        if (loadingCategory == true) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = GridCells.Fixed(2)
        ) {
            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
            category?.let {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(Modifier.padding(horizontal = 15.dp, vertical = 25.dp)) {
                        Text(text = category.name, fontSize = 18.sp)
                    }
                }
                items(it.walls.size) { index ->
                    val wall = it.walls[index]

                    wall.thumbnail_url?.let { fileURL ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp, horizontal = 3.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    if (!isDrawerActive) {
                                        makeCategoryWallScreenActive(true, index)
                                    } else {
                                        openDrawer(false)
                                    }
                                },
                            contentAlignment = Alignment.BottomStart
                        ) {
                            WallpaperImage(
                                imageURL = fileURL,
                                imageDescription = wall.file_name,
                                height = 220.dp
                            )
                        }
                    }
                }
            }
        }
    }
}