package com.paraskcd.unitedwalls.screens

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.paraskcd.unitedwalls.R
import com.paraskcd.unitedwalls.components.WallpaperBackground
import com.paraskcd.unitedwalls.components.WallpaperScreenImage
import com.paraskcd.unitedwalls.viewmodel.CategoryViewModel
import com.paraskcd.unitedwalls.viewmodel.UploadersViewModel
import com.paraskcd.unitedwalls.viewmodel.WallsViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule

@OptIn(ExperimentalSnapperApi::class, ExperimentalCoilApi::class, ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun WallScreen(wallScreenActive: Boolean, makeWallScreenActive: (Boolean) -> Unit, wallsViewModel: WallsViewModel, categoryViewModel: CategoryViewModel, uploadersViewModel: UploadersViewModel) {
    val lazyListState = rememberLazyListState()
    val walls = wallsViewModel.walls.observeAsState().value
    val wallIndex = wallsViewModel.selectedWallIndex.value
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val favouriteWalls = wallsViewModel.favouriteWalls.collectAsState().value
    var infoState: Boolean by remember { mutableStateOf(false) }
    val categories = categoryViewModel.categories.observeAsState().value
    val storagePermission = rememberPermissionState(permission = android.Manifest.permission.READ_EXTERNAL_STORAGE)
    val username = uploadersViewModel.uploaderUsername.observeAsState().value
    var thumbnailBackground: String? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = wallScreenActive) {
        Timer().schedule(0) {
            coroutineScope.launch {
                lazyListState.scrollToItem(index = wallIndex)
                if (walls != null) {
                    uploadersViewModel.getUploaderThroughWall(walls[wallIndex]._id)
                    thumbnailBackground = walls[wallIndex].thumbnail_url
                }
            }
        }
    }
    
    LaunchedEffect(key1 = lazyListState.firstVisibleItemIndex) {
        Timer().schedule(0) {
            coroutineScope.launch {
                if (walls != null) {
                    uploadersViewModel.getUploaderThroughWall(walls[lazyListState.firstVisibleItemIndex]._id)
                    thumbnailBackground = walls[lazyListState.firstVisibleItemIndex].thumbnail_url
                }
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
            if (thumbnailBackground != null) {
                WallpaperBackground(imageURL = thumbnailBackground!!, imageDescription = thumbnailBackground!!)
            }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(MaterialTheme.colorScheme.primary, Color.Transparent)
                        )
                    )
                ) {
                    IconButton(onClick = {
                        makeWallScreenActive(false)
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = "Arrow",
                            modifier = Modifier
                                .padding(6.dp)
                                .size(18.dp),
                            colorFilter = ColorFilter.tint(color = Color.White)
                        )
                    }
                }

            }
            LazyRow(
                modifier = Modifier.padding(vertical = 32.dp),
                state = lazyListState,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
            ) {
                walls?.size?.let {
                    items(it) { index ->
                        val wall = walls[index]
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
                                    .fillMaxSize(),
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    category?.let {
                                        Text(text = category, color = Color.White)
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
                                            Text(text = " $username", modifier = Modifier
                                                .padding(top = 6.dp, bottom = 12.dp))
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
                                            if (!liked) {
                                                wallsViewModel.addWallToFavourites(wall._id)
                                                liked = true
                                                Toast.makeText(context, "Wallpaper added to your Favourites! :)", Toast.LENGTH_LONG).show()
                                                wallsViewModel.getPopulatedFavouriteWalls()
                                                coroutineScope.launch {
                                                    wallsViewModel.addToServer(wall._id, "addFav")
                                                }
                                            } else {
                                                for (wallF in favouriteWalls) {
                                                    if (wall._id == wallF.wallpaperId) {
                                                        wallsViewModel.removeWallFromFavourites(wallF)
                                                        liked = false
                                                        Toast.makeText(context, "Wallpaper removed from your Favourites! :)", Toast.LENGTH_LONG).show()
                                                        wallsViewModel.getPopulatedFavouriteWalls()
                                                        coroutineScope.launch {
                                                            wallsViewModel.addToServer(wall._id, "removeFav")
                                                        }
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
            }
        }
    }
}

@Throws(IOException::class)
fun saveBitmap(
    context: Context,
    bitmap: Bitmap,
    format: Bitmap.CompressFormat,
    mimeType: String,
    displayName: String
): Uri {
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/UnitedWalls")
    }

    val resolver = context.contentResolver
    var uri: Uri? = null

    try {
        if (getExistingImageUriOrNullQ(context, displayName) == null) {
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: throw IOException("Failed to create new MediaStore record.")

            resolver.openOutputStream(uri)?.use {
                if (!bitmap.compress(format, 100, it))
                    throw IOException("Failed to save bitmap.")
            } ?: throw IOException("Failed to open output stream.")

            return uri
        } else {
            return getExistingImageUriOrNullQ(context, displayName)!!
        }
    } catch (e: IOException) {
        uri?.let { orphanUri ->
            resolver.delete(orphanUri, null, null)
        }

        throw e
    }
}

private fun getExistingImageUriOrNullQ(context: Context, displayName: String): Uri? {
    val projection = arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DISPLAY_NAME,   // unused (for verification use only)
        MediaStore.MediaColumns.RELATIVE_PATH,  // unused (for verification use only)
        MediaStore.MediaColumns.DATE_MODIFIED   //used to set signature for Glide
    )
    val selection = "${MediaStore.MediaColumns.RELATIVE_PATH}='${Environment.DIRECTORY_PICTURES}/UnitedWalls/' AND " + "${MediaStore.MediaColumns.DISPLAY_NAME}='$displayName.jpg' "

    val contentResolver = context.contentResolver

    contentResolver.query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection, selection, null, null ).use { c ->
        if (c != null && c.count >= 1) {
            c.moveToFirst().let {
                val id = c.getLong(c.getColumnIndexOrThrow(MediaStore.MediaColumns._ID) )
                val imageUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  id)
                return imageUri
            }
        }
    }
    return null
}