package com.paraskcd.unitedwalls.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.unitedwalls.components.Screen
import com.paraskcd.unitedwalls.components.WallpaperImage
import com.paraskcd.unitedwalls.model.Uploader
import com.paraskcd.unitedwalls.viewmodel.UploadersViewModel

@Composable
fun UploaderScreen(
    uploadersViewModel: UploadersViewModel,
    openDrawer: (Boolean) -> Unit,
    isDrawerActive: Boolean,
    screenActive: Int,
    uploader: Uploader?,
    uploaderWallsCount: Int?,
    loadingUploader: Boolean?,
    makeUploaderWallScreenActive: (flag: Boolean, index: Int) -> Unit
) {
    val lifecycleState = LocalLifecycleOwner.current.lifecycle.observeAsState()
    val state = lifecycleState.value

    LaunchedEffect(key1 = state) {
        if (state.toString() == "ON_RESUME") {
            uploadersViewModel.resetPage()
            if (uploader != null) {
                uploadersViewModel.getUploaderWallsData(uploader._id)
                Log.d("Wallss", uploader.username)
            }
        }
    }

    Screen(
        openDrawer = openDrawer,
        isDrawerActive = isDrawerActive,
        screenActive = screenActive,
        screenIndex = 10
    ) {
        if (loadingUploader == true) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2)
        ) {
            uploader?.let {
                if (uploader.avatar_file_url != null) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 5.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            WallpaperImage(
                                imageURL = uploader.avatar_file_url,
                                imageDescription = uploader.username,
                                height = 320.dp
                            )
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
                                    )
                            )
                        }
                    }
                } else {
                    item {
                        Spacer(modifier = Modifier.height(70.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(70.dp))
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(Modifier.padding(horizontal = 15.dp, vertical = 25.dp)) {
                        Text(text = uploader.username, fontSize = 18.sp)
                    }
                }
                uploader.walls?.size?.let {
                    items(it) { index ->
                        val wall = uploader.walls[index]

                        wall.thumbnail_url?.let { fileURL ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp, horizontal = 3.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        if (!isDrawerActive) {
                                            uploadersViewModel.selectWallIndex(index)
                                            makeUploaderWallScreenActive(true, index)
                                        } else {
                                            openDrawer(false)
                                        }
                                    },
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

                item {
                    LaunchedEffect(true) {
                        if (uploaderWallsCount != null && uploaderWallsCount != uploader.walls.size) {
                            uploadersViewModel.getMoreUploaderWallsData(uploader._id)
                        }
                    }
                }
            }
        }
    }
}