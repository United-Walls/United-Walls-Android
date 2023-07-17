package com.paraskcd.unitedwalls.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paraskcd.unitedwalls.components.NetworkImage
import com.paraskcd.unitedwalls.components.Screen
import com.paraskcd.unitedwalls.components.WallpaperImage
import com.paraskcd.unitedwalls.viewmodel.UploadersViewModel

@Composable
fun Uploaders(
    openDrawer: (Boolean) -> Unit,
    isDrawerActive: Boolean,
    screenActive: Int,
    uploadersViewModel: UploadersViewModel,
    makeUploadersScreenActive: (id: String) -> Unit
) {
    val uploaders = uploadersViewModel.uploaders.observeAsState().value
    val loadingUploaders = uploadersViewModel.loadingUploaders.observeAsState().value

    Screen(
        openDrawer = openDrawer,
        isDrawerActive = isDrawerActive,
        screenActive = screenActive,
        screenIndex = 9
    ) {
        if (loadingUploaders == true) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        LazyVerticalGrid(modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2)) {
            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
            item {
                Spacer(modifier = Modifier.height(70.dp))
            }

            uploaders?.size?.let {
                items(it) { index ->
                    val uploader = uploaders[index]

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                makeUploadersScreenActive(uploader._id)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uploader.walls.isNotEmpty()) {
                            uploader.walls[0].thumbnail_url?.let { fileURL ->
                                WallpaperImage(
                                    imageURL = fileURL,
                                    imageDescription = uploader.walls[0].file_name,
                                    height = 100.dp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .alpha(0.50F)
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            if (uploader.avatar_file_url != null) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                    NetworkImage(
                                        imageURL = uploader.avatar_file_url,
                                        imageDescription = uploader.username,
                                        size = 24.dp,
                                        shape = CircleShape
                                    )
                                    Text(text = uploader.username, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Text(text = uploader.username, fontWeight = FontWeight.Bold)
                            }
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