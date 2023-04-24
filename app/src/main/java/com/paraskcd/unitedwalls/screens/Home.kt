package com.paraskcd.unitedwalls.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.paraskcd.unitedwalls.R

@Composable
fun Home(
    openDrawer: (Boolean) -> Unit,
    isDrawerActive: Boolean,
    screenActive: Int,
    wallsViewModel: WallsViewModel,
    makeWallScreenActive: (Boolean) -> Unit,
    makeWallOfDayScreenActive: (Boolean) -> Unit,
    makeMostLikedWallScreenActive: (Boolean) -> Unit,
    makeMostDownloadedWallScreenActive: (Boolean) -> Unit,
    makeMostLikedActive: () -> Unit,
    makeMostDownloadedActive: () -> Unit
) {
    val walls = wallsViewModel.walls.observeAsState().value
    val totalWalls = wallsViewModel.totalWalls.observeAsState().value
    val loadingWalls = wallsViewModel.loadingWalls.observeAsState().value
    val mostLikedWalls = wallsViewModel.mostFavouritedWalls.observeAsState().value
    val mostPopularWalls = wallsViewModel.mostDownloadedWalls.observeAsState().value
    val wallOfTheDay = wallsViewModel.wallOfTheDay.observeAsState().value
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
            item(span = { GridItemSpan(maxLineSpan) }) {
                if (wallOfTheDay != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                if (!isDrawerActive) {
                                    makeWallOfDayScreenActive(true)
                                } else {
                                    openDrawer(false)
                                }
                            },
                        contentAlignment = Alignment.BottomStart

                    ) {
                        wallOfTheDay.file_url?.let {
                            WallpaperImage(
                                imageURL = it,
                                imageDescription = wallOfTheDay.file_name,
                                height = 370.dp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.primary
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Text("Wall of the Day", modifier = Modifier.padding(18.dp))
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
            mostLikedWalls?.take(5)?.size.let {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(Modifier.padding(horizontal = 15.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                       Text(text = "Most Liked", fontSize = 18.sp)
                        
                        Button(onClick = { makeMostLikedActive() }) {
                            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Show More")
                                Image(
                                    painter = painterResource(id = R.drawable.arrow),
                                    contentDescription = "Arrow",
                                    modifier = Modifier
                                        .scale(scaleX = -1f, scaleY = 1f)
                                        .padding(6.dp)
                                        .size(18.dp)

                                )
                            }

                        }
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    LazyRow {
                        if (it != null) {
                            items(it) { index: Int ->
                                val wall = mostLikedWalls?.get(index)

                                wall?.thumbnail_url?.let { fileURL ->
                                    Box(
                                        modifier = Modifier
                                            .width(170.dp)
                                            .padding(vertical = 3.dp, horizontal = 3.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable {
                                                if (!isDrawerActive) {
                                                    wallsViewModel.selectWallIndex(index)
                                                    makeMostLikedWallScreenActive(true)
                                                } else {
                                                    openDrawer(false)
                                                }
                                            },
                                        contentAlignment = Alignment.BottomStart
                                    ) {
                                        WallpaperImage(
                                            imageURL = fileURL,
                                            imageDescription = wall.file_name,
                                            height = 270.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            mostPopularWalls?.take(5)?.size?.let {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(Modifier.padding(horizontal = 15.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Most Popular", fontSize = 18.sp)

                        Button(onClick = { makeMostDownloadedActive() }) {
                            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Show More")
                                Image(
                                    painter = painterResource(id = R.drawable.arrow),
                                    contentDescription = "Arrow",
                                    modifier = Modifier
                                        .scale(scaleX = -1f, scaleY = 1f)
                                        .padding(6.dp)
                                        .size(18.dp)

                                )
                            }

                        }
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    LazyRow {
                        items(it) { index: Int ->
                            val wall = mostPopularWalls[index]

                            wall.thumbnail_url?.let { fileURL ->
                                Box(
                                    modifier = Modifier
                                        .width(170.dp)
                                        .padding(vertical = 3.dp, horizontal = 3.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            if (!isDrawerActive) {
                                                wallsViewModel.selectWallIndex(index)
                                                makeMostDownloadedWallScreenActive(true)
                                            } else {
                                                openDrawer(false)
                                            }
                                        },
                                    contentAlignment = Alignment.BottomStart
                                ) {
                                    WallpaperImage(
                                        imageURL = fileURL,
                                        imageDescription = wall.file_name,
                                        height = 270.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(Modifier.padding(horizontal = 15.dp, vertical = 25.dp)) {
                    Text(text = "All Wallpapers", fontSize = 18.sp)
                }
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