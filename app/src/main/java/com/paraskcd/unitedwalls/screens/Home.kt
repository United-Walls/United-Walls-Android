package com.paraskcd.unitedwalls.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.paraskcd.unitedwalls.components.Screen
import com.paraskcd.unitedwalls.components.WallpaperImage
import com.paraskcd.unitedwalls.viewmodel.WallsViewModel

@Composable
fun Home(openDrawer: (Boolean) -> Unit, isDrawerActive: Boolean, screenActive: Int, wallsViewModel: WallsViewModel, makeWallScreenActive: (Boolean) -> Unit) {
    val walls = wallsViewModel.walls.observeAsState().value
    val loadingWalls = wallsViewModel.loadingWalls.observeAsState().value

    Screen(
        openDrawer = openDrawer,
        isDrawerActive = isDrawerActive,
        screenActive = screenActive,
        screenIndex = 0
    ) {
        if (loadingWalls == true) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        LazyColumn {
            walls?.size?.let {
                items(it) { index ->
                    val wall = walls[index]
                    
                    wall.file_url?.let { fileURL -> 
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    if (!isDrawerActive) {
                                        wallsViewModel.selectWallIndex(index)
                                        makeWallScreenActive(true)
                                    }
                                },
                            contentAlignment = Alignment.BottomStart
                        ) {
                            WallpaperImage(
                                imageURL = fileURL,
                                imageDescription = wall.file_name,
                                height = 420.dp,
                                extraKeys = "Home"
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                MaterialTheme.colorScheme.primary
                                            )
                                        )
                                    )
                                    .alpha(0.7f)
                            )
                            Text(
                                text = wall.file_name,
                                modifier = Modifier.padding(24.dp)
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }
    }
}