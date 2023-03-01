package com.paraskcd.unitedwalls.screens

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.imageLoader
import com.paraskcd.unitedwalls.R
import com.paraskcd.unitedwalls.components.WallpaperScreenImage
import com.paraskcd.unitedwalls.model.Category
import com.paraskcd.unitedwalls.viewmodel.WallsViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapOffsets
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun CategoryWallScreen(
    categoryWallScreenActive: Boolean,
    makeCategoryWallScreenActive: (Boolean) -> Unit,
    category: Category?,
    categoryWallIndex: Int,
    wallsViewModel: WallsViewModel
) {
    val lazyListState = rememberLazyListState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val favouriteWalls = wallsViewModel.favouriteWalls.collectAsState().value

    LaunchedEffect(key1 = categoryWallScreenActive) {
        Timer().schedule(0) {
            coroutineScope.launch {
                lazyListState.scrollToItem(index = categoryWallIndex)
            }
        }
    }

    BackHandler(enabled = categoryWallScreenActive) {
        makeCategoryWallScreenActive(false)
    }

    AnimatedVisibility(
        visible = categoryWallScreenActive,
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
                    snapOffsetForItem = SnapOffsets.Center,
                )
            ) {
                category?.let {
                    items(it.walls.size) { index ->
                        val wall = it.walls[index]

                        wall.file_url?.let { fileURL ->
                            var liked: Boolean by remember {
                                mutableStateOf(false)
                            }

                            if (favouriteWalls.isNotEmpty()) {
                                for (wallF in favouriteWalls) {
                                    if (wallF.wallpaperId == wall._id) {
                                        liked = true
                                        break
                                    } else {
                                        liked = false
                                    }
                                }
                            }

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
                                    modifier = Modifier.padding(end = 24.dp, bottom = 24.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (!liked) {
                                                wallsViewModel.addWallToFavourites(wall._id)
                                                liked = true
                                                Toast.makeText(context, "Wallpaper added to your Favourites! :)", Toast.LENGTH_LONG).show()
                                                wallsViewModel.getPopulatedFavouriteWalls()
                                            } else {
                                                for (wallF in favouriteWalls) {
                                                    if (wall._id == wallF.wallpaperId) {
                                                        wallsViewModel.removeWallFromFavourites(wallF)
                                                        liked = false
                                                        Toast.makeText(context, "Wallpaper removed from your Favourites! :)", Toast.LENGTH_LONG).show()
                                                        wallsViewModel.getPopulatedFavouriteWalls()
                                                        break
                                                    }
                                                }
                                            }
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary), modifier = Modifier
                                            .width(40.dp)
                                            .height(40.dp)
                                            .padding(bottom = 6.dp)
                                            .alpha(0.50f)
                                    ) {
                                        Image(
                                            painter = painterResource(id = if (!liked) R.drawable.heart else R.drawable.heartfilled),
                                            contentDescription = "Favourite",
                                            modifier = Modifier
                                                .padding(6.dp)
                                                .size(40.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            val shareIntent: Intent = Intent(Intent.ACTION_SEND)
                                            context.imageLoader.diskCache?.get(wall.file_url)?.use { snapshot ->
                                                val imageFile = snapshot.data.toFile()
                                                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check this amazing Wallpaper from the United Walls App! :)");
                                                shareIntent.setType("image/png");
                                                shareIntent.putExtra(Intent.EXTRA_STREAM, saveBitmap(context, BitmapFactory.decodeFile(imageFile.path), Bitmap.CompressFormat.PNG, "image/png", wall.file_name))
                                            }
                                            context.startActivity(shareIntent)
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary), modifier = Modifier
                                            .width(40.dp)
                                            .height(40.dp)
                                            .padding(bottom = 6.dp)
                                            .alpha(0.50f)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.share),
                                            contentDescription = "Favourite",
                                            modifier = Modifier
                                                .padding(6.dp)
                                                .size(40.dp)
                                        )
                                    }
                                    // Download Button
                                    IconButton(
                                        onClick = {
                                            context.imageLoader.diskCache?.get(wall.file_url)?.use { snapshot ->
                                                val imageFile = snapshot.data.toFile()
                                                saveBitmap(context = context, bitmap = BitmapFactory.decodeFile(imageFile.path), format = Bitmap.CompressFormat.PNG, mimeType = "image/png", displayName = wall.file_name)
                                                Toast.makeText(context, "Wallpaper added to your Gallery! :)", Toast.LENGTH_LONG).show()
                                            }
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary), modifier = Modifier
                                            .width(40.dp)
                                            .height(40.dp)
                                            .padding(bottom = 6.dp)
                                            .alpha(0.50f)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.download),
                                            contentDescription = "Favourite",
                                            modifier = Modifier
                                                .padding(6.dp)
                                                .size(40.dp)
                                        )
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
                    makeCategoryWallScreenActive(false)
                }) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Menu Icon")
                }
            }
        }
    }
}