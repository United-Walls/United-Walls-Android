package com.paraskcd.unitedwalls.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paraskcd.unitedwalls.components.Screen
import com.paraskcd.unitedwalls.components.WallpaperImage
import com.paraskcd.unitedwalls.viewmodel.CategoryViewModel

@Composable
fun Categories(
    openDrawer: (Boolean) -> Unit,
    isDrawerActive: Boolean,
    screenActive: Int,
    categoryViewModel: CategoryViewModel,
    makeCategoryScreenActive: (id: String) -> Unit
) {
    val categories = categoryViewModel.categories.observeAsState().value?.filter { it.walls.isNullOrEmpty().not() }
    val loadingCategories = categoryViewModel.loadingCategories.observeAsState().value

    Screen(
        openDrawer = openDrawer,
        isDrawerActive = isDrawerActive,
        screenActive = screenActive,
        screenIndex = 1
    ) {
        if (loadingCategories == true) {
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
            categories?.size?.let {
                items(it) { index ->
                    val category = categories[index]

                    if (category.walls.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    makeCategoryScreenActive(category._id)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            category.walls[0].thumbnail_url?.let { fileURL ->
                                WallpaperImage(
                                    imageURL = fileURL,
                                    imageDescription = category.walls[0].file_name,
                                    height = 100.dp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .alpha(0.50F)
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Text(text = category.name, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}