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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.paraskcd.unitedwalls.R
import com.paraskcd.unitedwalls.components.WallpaperScreenImage
import com.paraskcd.unitedwalls.viewmodel.CategoryViewModel
import com.paraskcd.unitedwalls.viewmodel.WallsViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapOffsets
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

@OptIn(ExperimentalSnapperApi::class, ExperimentalCoilApi::class)
@Composable
fun FavouriteWallScreen(
    favouriteWallScreenActive: Boolean,
    makeFavouriteWallScreenActive: (Boolean) -> Unit,
    wallsViewModel: WallsViewModel,
    categoryViewModel: CategoryViewModel
) {
    val lazyListState = rememberLazyListState()
    val walls = wallsViewModel.favouritePopulatedWallsStore
    val wallIndex = wallsViewModel.selectedFavouriteWallIndex.value
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val favouriteWalls = wallsViewModel.favouriteWalls.collectAsState().value
    var infoState: Boolean by remember { mutableStateOf(false) }
    val categories = categoryViewModel.categories.observeAsState().value

    LaunchedEffect(key1 = favouriteWallScreenActive) {
        Timer().schedule(0) {
            coroutineScope.launch {
                lazyListState.scrollToItem(index = wallIndex)
            }
        }
    }

    BackHandler(enabled = favouriteWallScreenActive) {
        makeFavouriteWallScreenActive(false)
    }

    AnimatedVisibility(
        visible = favouriteWallScreenActive,
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
                itemsIndexed(walls) { index, wall ->
                    wall.file_url?.let { fileURL ->
                        val wallName = wall.file_name.replace('_', ' ')
                        var category: String? = null

                        if (categories != null) {
                            for (cat in categories) {
                                if (cat._id == wall.category) {
                                    category = cat.name
                                    break
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
                                category?.let {
                                    Text(text = category)
                                }
                                WallpaperScreenImage(
                                    imageURL = fileURL,
                                    imageDescription = wall.file_name,
                                    width = screenWidth
                                )
                            }
                            AnimatedVisibility(
                                visible = infoState,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .width(screenWidth)
                                        .padding(bottom = 18.dp)
                                        .alpha(0.85f)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(start = 18.dp)
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 12.dp,
                                                    topEnd = 12.dp,
                                                    bottomStart = if (wall.addedBy == null) 12.dp else 0.dp,
                                                    bottomEnd = if (wall.addedBy == null) 12.dp else 0.dp
                                                )
                                            )
                                            .background(MaterialTheme.colorScheme.primary)
                                            .width(230.dp)
                                    ) {
                                        Text(text = "Name -", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 6.dp))
                                        Text(text = " $wallName", modifier = Modifier
                                            .padding(top = 12.dp, bottom = 6.dp))
                                    }
                                    wall.addedBy?.let { addedBy ->
                                        Row(
                                            modifier = Modifier
                                                .padding(start = 18.dp)
                                                .clip(
                                                    RoundedCornerShape(
                                                        bottomStart = 12.dp,
                                                        bottomEnd = 12.dp
                                                    )
                                                )
                                                .background(MaterialTheme.colorScheme.primary)
                                                .width(230.dp)
                                        ) {
                                            Text(text = "Added By -", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp, top = 6.dp, bottom = 12.dp))
                                            Text(text = " $addedBy", modifier = Modifier
                                                .padding(top = 6.dp, bottom = 12.dp))
                                        }
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier.padding(end = 24.dp, bottom = 24.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        infoState = !infoState
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(40.dp)
                                        .padding(bottom = 6.dp)
                                        .alpha(0.50f)
                                ) {
                                    Image(
                                        painter = painterResource(id = if (!infoState) R.drawable.about else R.drawable.aboutfilled),
                                        contentDescription = "Info",
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .size(40.dp)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        for (wallF in favouriteWalls) {
                                            if (wall._id == wallF.wallpaperId) {
                                                wallsViewModel.removeWallFromFavourites(wallF)
                                                Toast.makeText(context, "Wallpaper removed from your Favourites! :)", Toast.LENGTH_LONG).show()
                                                wallsViewModel.getPopulatedFavouriteWalls()
                                                coroutineScope.launch {
                                                    wallsViewModel.addToServer(wall._id, "removeFav")
                                                }
                                                break
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
                                        painter = painterResource(id = R.drawable.heartfilled),
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
                                            shareIntent.setType("image/jpeg");
                                            shareIntent.putExtra(Intent.EXTRA_STREAM, saveBitmap(context = context, bitmap = BitmapFactory.decodeFile(imageFile.path), format = if (wall.mime_type == "image/jpeg") Bitmap.CompressFormat.JPEG else if (wall.mime_type == "image/png") Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.WEBP, mimeType = wall.mime_type, displayName = wall.file_name))
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
                                            saveBitmap(context = context, bitmap = BitmapFactory.decodeFile(imageFile.path), format = if (wall.mime_type == "image/jpeg") Bitmap.CompressFormat.JPEG else if (wall.mime_type == "image/png") Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.WEBP, mimeType = wall.mime_type, displayName = wall.file_name)
                                            Toast.makeText(context, "Wallpaper added to your Gallery! :)", Toast.LENGTH_LONG).show()
                                            coroutineScope.launch {
                                                wallsViewModel.addToServer(wall._id, "addDownloaded")
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
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(onClick = {
                    makeFavouriteWallScreenActive(false)
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Arrow",
                        modifier = Modifier
                            .padding(6.dp)
                            .size(18.dp)
                    )
                }
            }
        }
    }
}