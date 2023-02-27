package com.paraskcd.unitedwalls.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.paraskcd.unitedwalls.components.WallpaperScreenImage
import com.paraskcd.unitedwalls.viewmodel.WallsViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapOffsets
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.paraskcd.unitedwalls.R

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun WallScreen(wallScreenActive: Boolean, makeWallScreenActive: (Boolean) -> Unit, wallsViewModel: WallsViewModel) {
    val lazyListState = rememberLazyListState()
    val walls = wallsViewModel.walls.observeAsState().value
    val wallIndex = wallsViewModel.selectedWallIndex.value
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = wallScreenActive) {
        Timer().schedule(500) {
            coroutineScope.launch {
                lazyListState.scrollToItem(index = wallIndex)
            }
        }
    }

    BackHandler(enabled = wallScreenActive) {
        makeWallScreenActive(false)
    }

    AnimatedVisibility(
        visible = wallScreenActive,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.BottomEnd
        ) {
            LazyRow(
                modifier = Modifier.padding(vertical = 32.dp),
                state = lazyListState,
                flingBehavior = rememberSnapperFlingBehavior(
                    lazyListState = lazyListState,
                    snapOffsetForItem = SnapOffsets.Start,
                )
            ) {
                walls?.size?.let {
                    items(it) { index ->
                        val wall = walls[index]

                        wall.file_url?.let { fileURL ->
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = wall.file_name)
                                    WallpaperScreenImage(
                                        imageURL = fileURL,
                                        imageDescription = wall.file_name,
                                        width = screenWidth
                                    )
                                }
                                Column(
                                    modifier = Modifier.padding(end = 24.dp)
                                ) {
                                    IconButton(
                                        onClick = { /*TODO*/ },
                                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary), modifier = Modifier.size(48.dp).padding(bottom = 6.dp)
                                    ) {
                                        Image(painter = painterResource(id = R.drawable.heart), contentDescription = "Favourite", modifier = Modifier.padding(12.dp))
                                    }
                                    IconButton(
                                        onClick = { /*TODO*/ },
                                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary), modifier = Modifier.size(48.dp).padding(bottom = 6.dp)
                                    ) {
                                        Image(painter = painterResource(id = R.drawable.heart), contentDescription = "Favourite", modifier = Modifier.padding(12.dp))
                                    }
                                    IconButton(
                                        onClick = { /*TODO*/ },
                                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary), modifier = Modifier.size(48.dp).padding(bottom = 6.dp)
                                    ) {
                                        Image(painter = painterResource(id = R.drawable.heart), contentDescription = "Favourite", modifier = Modifier.padding(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(onClick = {
                    makeWallScreenActive(false)
                }) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Menu Icon")
                }
            }
        }
    }
}