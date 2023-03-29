package com.paraskcd.unitedwalls.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.paraskcd.unitedwalls.BuildConfig
import com.paraskcd.unitedwalls.components.Screen
import com.paraskcd.unitedwalls.components.WallpaperImage
import com.paraskcd.unitedwalls.utils.Constants
import com.paraskcd.unitedwalls.viewmodel.WallsViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun Home(
    openDrawer: (Boolean) -> Unit,
    isDrawerActive: Boolean,
    screenActive: Int,
    wallsViewModel: WallsViewModel,
    makeWallScreenActive: (Boolean) -> Unit,
) {
    val walls = wallsViewModel.walls.observeAsState().value
    val totalWalls = wallsViewModel.totalWalls.observeAsState().value
    val loadingWalls = wallsViewModel.loadingWalls.observeAsState().value
    val scrollState = rememberLazyListState()
    val lifecycleState = LocalLifecycleOwner.current.lifecycle.observeAsState()
    val state = lifecycleState.value

    LaunchedEffect(key1 = state) {
        if (state.toString() == "ON_RESUME") {
            wallsViewModel.resetPage()
            wallsViewModel.getWallsData()
        }
    }

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
            walls?.size?.let {
                items(it) { index ->
                    val wall = walls[index]
                    
                    wall.thumbnail_url?.let { fileURL ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp, horizontal = 3.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    if (!isDrawerActive) {
                                        wallsViewModel.selectWallIndex(index)
                                        makeWallScreenActive(true)
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
                item { 
                    LaunchedEffect(true) {
                        if (totalWalls != walls.size) {
                            wallsViewModel.getMoreData()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Lifecycle.observeAsState(): State<Lifecycle.Event> {
    val state = remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(this) {
        val observer = LifecycleEventObserver { _, event ->
            state.value = event
        }
        this@observeAsState.addObserver(observer)
        onDispose {
            this@observeAsState.removeObserver(observer)
        }
    }

    return state
}