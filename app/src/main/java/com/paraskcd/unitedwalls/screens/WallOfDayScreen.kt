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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import coil.imageLoader
import com.paraskcd.unitedwalls.components.WallpaperScreenImage
import com.paraskcd.unitedwalls.viewmodel.CategoryViewModel
import com.paraskcd.unitedwalls.viewmodel.WallsViewModel
import kotlinx.coroutines.launch

@Composable
fun WallOfDayScreen(wallOfDayScreenActive: Boolean, makeWallOfDayScreenActive: (Boolean) -> Unit, wallsViewModel: WallsViewModel, categoryViewModel: CategoryViewModel) {
    val wallOfDay = wallsViewModel.wallOfTheDay.observeAsState().value
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val context = LocalContext.current
    val favouriteWalls = wallsViewModel.favouriteWalls.collectAsState().value
    var infoState: Boolean by remember { mutableStateOf(false) }
    val categories = categoryViewModel.categories.observeAsState().value
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = wallOfDayScreenActive) {
        makeWallOfDayScreenActive(false)
    }

    AnimatedVisibility(
        visible = wallOfDayScreenActive,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (wallOfDay != null) {
                    var category: String? = null
                    var liked: Boolean by remember {
                        mutableStateOf(false)
                    }

                    if (favouriteWalls.isNotEmpty()) {
                        for (wallF in favouriteWalls) {
                            if (wallF.wallpaperId == wallOfDay._id) {
                                liked = true
                                break
                            } else {
                                liked = false
                            }
                        }
                    }

                    if (categories != null) {
                        for (cat in categories) {
                            if (cat._id == wallOfDay.category) {
                                category = cat.name
                                break
                            }
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 18.dp)
                    ) {
                        if (category != null) {
                            Text(text = category)
                        }
                        wallOfDay.file_url?.let {
                            WallpaperScreenImage(
                                imageURL = it,
                                imageDescription = wallOfDay.file_name,
                                width = screenWidth
                            )
                        }
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
                                            bottomStart = if (wallOfDay.addedBy == null) 12.dp else 0.dp,
                                            bottomEnd = if (wallOfDay.addedBy == null) 12.dp else 0.dp
                                        )
                                    )
                                    .background(MaterialTheme.colorScheme.primary)
                                    .width(230.dp)
                            ) {
                                Text(text = "Name -", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 6.dp))
                                Text(text = " ${wallOfDay.file_name}", modifier = Modifier
                                    .padding(top = 12.dp, bottom = 6.dp))
                            }
                            wallOfDay.addedBy?.let { addedBy ->
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
                        modifier = Modifier.padding(end = 30.dp, bottom = 40.dp)
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
                                painter = painterResource(id = if (!infoState) com.paraskcd.unitedwalls.R.drawable.about else com.paraskcd.unitedwalls.R.drawable.aboutfilled),
                                contentDescription = "Info",
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(40.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                if (!liked) {
                                    wallsViewModel.addWallToFavourites(wallOfDay._id)
                                    liked = true
                                    Toast.makeText(context, "Wallpaper added to your Favourites! :)", Toast.LENGTH_LONG).show()
                                    coroutineScope.launch {
                                        wallsViewModel.addToServer(wallOfDay._id, "addFav")
                                    }
                                    wallsViewModel.getPopulatedFavouriteWalls()
                                } else {
                                    for (wallF in favouriteWalls) {
                                        if (wallOfDay._id == wallF.wallpaperId) {
                                            wallsViewModel.removeWallFromFavourites(wallF)
                                            liked = false
                                            Toast.makeText(context, "Wallpaper removed from your Favourites! :)", Toast.LENGTH_LONG).show()
                                            coroutineScope.launch {
                                                wallsViewModel.addToServer(wallOfDay._id, "removeFav")
                                            }
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
                                painter = painterResource(id = if (!liked) com.paraskcd.unitedwalls.R.drawable.heart else com.paraskcd.unitedwalls.R.drawable.heartfilled),
                                contentDescription = "Favourite",
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(40.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                val shareIntent: Intent = Intent(Intent.ACTION_SEND)
                                wallOfDay.file_url?.let {
                                    context.imageLoader.diskCache?.get(it)?.use { snapshot ->
                                        val imageFile = snapshot.data.toFile()
                                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check this amazing Wallpaper from the United Walls App! :)");
                                        shareIntent.setType("image/jpeg");
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, saveBitmap(context = context, bitmap = BitmapFactory.decodeFile(imageFile.path), format = if (wallOfDay.mime_type == "image/jpeg") Bitmap.CompressFormat.JPEG else if (wallOfDay.mime_type == "image/png") Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.WEBP, mimeType = wallOfDay.mime_type, displayName = wallOfDay.file_name))
                                    }
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
                                painter = painterResource(id = com.paraskcd.unitedwalls.R.drawable.share),
                                contentDescription = "Favourite",
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(40.dp)
                            )
                        }
                        // Download Button
                        IconButton(
                            onClick = {
                                wallOfDay.file_url?.let {
                                    context.imageLoader.diskCache?.get(it)?.use { snapshot ->
                                        val imageFile = snapshot.data.toFile()
                                        saveBitmap(context = context, bitmap = BitmapFactory.decodeFile(imageFile.path), format = if (wallOfDay.mime_type == "image/jpeg") Bitmap.CompressFormat.JPEG else if (wallOfDay.mime_type == "image/png") Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.WEBP, mimeType = wallOfDay.mime_type, displayName = wallOfDay.file_name)
                                        Toast.makeText(context, "Wallpaper added to your Gallery! :)", Toast.LENGTH_LONG).show()
                                        coroutineScope.launch {
                                            wallsViewModel.addToServer(wallOfDay._id, "addDownloaded")
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
                                painter = painterResource(id = com.paraskcd.unitedwalls.R.drawable.download),
                                contentDescription = "Favourite",
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(40.dp)
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(onClick = {
                    makeWallOfDayScreenActive(false)
                }) {
                    Image(
                        painter = painterResource(id = com.paraskcd.unitedwalls.R.drawable.arrow),
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