package com.paraskcd.unitedwalls.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.unitedwalls.R
import com.paraskcd.unitedwalls.components.Container
import com.paraskcd.unitedwalls.components.NetworkImage
import com.paraskcd.unitedwalls.components.NetworkImageWithBorder
import com.paraskcd.unitedwalls.components.Screen
import com.paraskcd.unitedwalls.components.WallpaperImage
import com.paraskcd.unitedwalls.components.segmentedControl.SegmentText
import com.paraskcd.unitedwalls.components.segmentedControl.SegmentedControl
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
    val page = remember { listOf("Walls", "Info") }
    var selectedPage by remember { mutableStateOf("Walls") }
    val context = LocalContext.current

    LaunchedEffect(key1 = state) {
        if (state.toString() == "ON_RESUME") {
            uploadersViewModel.resetPage()
            if (uploader != null) {
                uploadersViewModel.getUploaderWallsData(uploader._id)
                Log.d("Wallss", uploader.username)
            }
        }
    }

    LaunchedEffect(key1 = screenActive) {
        selectedPage = "Walls"
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
                                .padding(bottom = 5.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            if (uploader.walls.isNotEmpty()) {
                                uploader.walls[0].file_url?.let { firstWall ->
                                    Box(
                                        modifier = Modifier.clip(RoundedCornerShape(corner = CornerSize(12.dp)))
                                    ) {
                                        WallpaperImage(
                                            imageURL = firstWall,
                                            imageDescription = uploader.walls[0].file_name,
                                            height = 320.dp
                                        )
                                    }

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
                                            ),
                                            shape = RoundedCornerShape(corner = CornerSize(12.dp))
                                        )
                                )
                            }
                            Box(modifier = Modifier
                                .size(180.dp)
                                .offset(y = 80.dp)) {
                                NetworkImageWithBorder(
                                    imageURL = uploader.avatar_file_url,
                                    imageDescription = uploader.username,
                                    size = 180.dp,
                                    shape = RoundedCornerShape(corner = CornerSize(20.dp))
                                )
                            }

                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(90.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(90.dp))
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
                    Row(Modifier.padding(horizontal = 15.dp, vertical = 5.dp), horizontalArrangement = Arrangement.Center) {
                        Text(text = "@" + uploader.username, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                }
                if (uploader.description?.isNotEmpty() == true) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(Modifier.padding(horizontal = 15.dp, vertical = 5.dp), horizontalArrangement = Arrangement.Center) {
                            Text(text = uploader.description, textAlign = TextAlign.Center)
                        }
                    }
                }
                if ((uploader.socialMediaLinks != null &&
                        (uploader.socialMediaLinks.facebook != null || uploader.socialMediaLinks.twitter != null || uploader.socialMediaLinks.instagram != null || uploader.socialMediaLinks.mastodon != null || uploader.socialMediaLinks.steam != null || uploader.socialMediaLinks.linkedIn != null || uploader.socialMediaLinks.link != null) &&
                        (uploader.socialMediaLinks.facebook != "" || uploader.socialMediaLinks.twitter != "" || uploader.socialMediaLinks.instagram != "" || uploader.socialMediaLinks.mastodon != "" || uploader.socialMediaLinks.steam != "" || uploader.socialMediaLinks.linkedIn != "" || uploader.socialMediaLinks.link != "" || !uploader.socialMediaLinks.other.isNullOrEmpty())) ||
                    (uploader.donationLinks != null &&
                        (uploader.donationLinks.patreon != null || uploader.donationLinks.paypal != null) &&
                        (uploader.donationLinks.patreon != "" || uploader.donationLinks.paypal != "" || !uploader.donationLinks.otherdonations.isNullOrEmpty()))) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        SegmentedControl(segments = page, selectedSegment = selectedPage, onSegmentSelected = { selectedPage = it }) {
                            SegmentText(text = it)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                if (selectedPage == "Walls") {
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
                } else {
                    if ((uploader.donationLinks != null &&
                                (uploader.donationLinks.patreon != null || uploader.donationLinks.paypal != null) &&
                                (uploader.donationLinks.patreon != "" || uploader.donationLinks.paypal != "" || !uploader.donationLinks.otherdonations.isNullOrEmpty()))) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Row(Modifier.padding(horizontal = 15.dp, vertical = 5.dp), horizontalArrangement = Arrangement.Start) {
                                Text(text = "Social Links", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Container(backgroundColor = MaterialTheme.colorScheme.primary) {
                                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.Start) {
                                    if (uploader.donationLinks.patreon != null && uploader.donationLinks.patreon != "")
                                        Row(modifier = Modifier
                                            .padding(vertical = 10.dp)
                                            .clickable {
                                                context.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(uploader.donationLinks.patreon)
                                                    )
                                                )
                                            }, horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                painter = painterResource(id = R.drawable.patreon),
                                                contentDescription = "Patreon",
                                                modifier = Modifier
                                                    .height(25.dp),
                                                contentScale = ContentScale.Fit,
                                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("Patreon")
                                        }

                                    if (uploader.donationLinks.paypal != null && uploader.donationLinks.paypal != "") {
                                        Row(modifier = Modifier
                                            .padding(vertical = 10.dp)
                                            .clickable {
                                                context.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(uploader.donationLinks.paypal)
                                                    )
                                                )
                                            }, horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                painter = painterResource(id = R.drawable.paypal),
                                                contentDescription = "Paypal",
                                                modifier = Modifier
                                                    .height(25.dp),
                                                contentScale = ContentScale.Fit,
                                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("Paypal")
                                        }
                                    }

                                    if (!uploader.donationLinks.otherdonations.isNullOrEmpty()) {
                                        Column {
                                            uploader.donationLinks.otherdonations.forEach { otherLink ->
                                                if (otherLink.link != null && otherLink.link != "") {
                                                    Row(
                                                        modifier = Modifier
                                                            .padding(vertical = 10.dp)
                                                            .clickable {
                                                                context.startActivity(
                                                                    Intent(
                                                                        Intent.ACTION_VIEW,
                                                                        Uri.parse(otherLink.link)
                                                                    )
                                                                )
                                                            },
                                                        horizontalArrangement = Arrangement.Start,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Image(
                                                            painter = painterResource(id = R.drawable.link_solid),
                                                            contentDescription = otherLink.title,
                                                            modifier = Modifier
                                                                .height(25.dp),
                                                            contentScale = ContentScale.Fit,
                                                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                                                        )
                                                        Spacer(modifier = Modifier.width(12.dp))
                                                        otherLink.title?.let { it1 -> Text(it1) }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if ((uploader.socialMediaLinks != null &&
                            (uploader.socialMediaLinks.facebook != null || uploader.socialMediaLinks.twitter != null || uploader.socialMediaLinks.instagram != null || uploader.socialMediaLinks.mastodon != null || uploader.socialMediaLinks.steam != null || uploader.socialMediaLinks.linkedIn != null || uploader.socialMediaLinks.link != null) &&
                            (uploader.socialMediaLinks.facebook != "" || uploader.socialMediaLinks.twitter != "" || uploader.socialMediaLinks.instagram != "" || uploader.socialMediaLinks.mastodon != "" || uploader.socialMediaLinks.steam != "" || uploader.socialMediaLinks.linkedIn != "" || uploader.socialMediaLinks.link != "" || !uploader.socialMediaLinks.other.isNullOrEmpty()))) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Row(Modifier.padding(horizontal = 15.dp, vertical = 5.dp), horizontalArrangement = Arrangement.Start) {
                                Text(text = "Social Links", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Container(backgroundColor = MaterialTheme.colorScheme.primary) {
                                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.Start) {
                                    if (uploader.socialMediaLinks.facebook != null && uploader.socialMediaLinks.facebook != "")
                                        Row(modifier = Modifier
                                            .padding(vertical = 10.dp)
                                            .clickable {
                                                context.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(uploader.socialMediaLinks.facebook)
                                                    )
                                                )
                                            }, horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                painter = painterResource(id = R.drawable.facebook),
                                                contentDescription = "Facebook",
                                                modifier = Modifier
                                                    .height(25.dp),
                                                contentScale = ContentScale.Fit,
                                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("Facebook")
                                        }

                                    if (uploader.socialMediaLinks.twitter != null && uploader.socialMediaLinks.twitter != "") {
                                        Row(modifier = Modifier
                                            .padding(vertical = 10.dp)
                                            .clickable {
                                                context.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(uploader.socialMediaLinks.twitter)
                                                    )
                                                )
                                            }, horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                painter = painterResource(id = R.drawable.x_twitter),
                                                contentDescription = "Twitter",
                                                modifier = Modifier
                                                    .height(25.dp),
                                                contentScale = ContentScale.Fit,
                                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("\uD835\uDD4F")
                                        }
                                    }

                                    if (uploader.socialMediaLinks.instagram != null && uploader.socialMediaLinks.instagram != "") {
                                        Row(modifier = Modifier
                                            .padding(vertical = 10.dp)
                                            .clickable {
                                                context.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(uploader.socialMediaLinks.instagram)
                                                    )
                                                )
                                            }, horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                painter = painterResource(id = R.drawable.instagram),
                                                contentDescription = "Instagram",
                                                modifier = Modifier
                                                    .height(25.dp),
                                                contentScale = ContentScale.Fit,
                                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("Instagram")
                                        }
                                    }

                                    if (uploader.socialMediaLinks.mastodon != null && uploader.socialMediaLinks.mastodon != "") {
                                        Row(modifier = Modifier
                                            .padding(vertical = 10.dp)
                                            .clickable {
                                                context.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(uploader.socialMediaLinks.mastodon)
                                                    )
                                                )
                                            }, horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                painter = painterResource(id = R.drawable.mastodon),
                                                contentDescription = "Mastodon",
                                                modifier = Modifier
                                                    .height(25.dp),
                                                contentScale = ContentScale.Fit,
                                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("Mastodon")
                                        }
                                    }

                                    if (uploader.socialMediaLinks.steam != null && uploader.socialMediaLinks.steam != "") {
                                        Row(modifier = Modifier
                                            .padding(vertical = 10.dp)
                                            .clickable {
                                                context.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(uploader.socialMediaLinks.steam)
                                                    )
                                                )
                                            }, horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                painter = painterResource(id = R.drawable.steam),
                                                contentDescription = "Steam",
                                                modifier = Modifier
                                                    .height(25.dp),
                                                contentScale = ContentScale.Fit,
                                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("Steam")
                                        }
                                    }

                                    if (uploader.socialMediaLinks.linkedIn != null && uploader.socialMediaLinks.linkedIn != "") {
                                        Row(modifier = Modifier
                                            .padding(vertical = 10.dp)
                                            .clickable {
                                                context.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(uploader.socialMediaLinks.linkedIn)
                                                    )
                                                )
                                            }, horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                painter = painterResource(id = R.drawable.linkedin),
                                                contentDescription = "LinkedIn",
                                                modifier = Modifier
                                                    .height(25.dp),
                                                contentScale = ContentScale.Fit,
                                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("LinkedIn")
                                        }
                                    }

                                    if (uploader.socialMediaLinks.link != null && uploader.socialMediaLinks.link != "") {
                                        Row(modifier = Modifier
                                            .padding(vertical = 10.dp)
                                            .clickable {
                                                context.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(uploader.socialMediaLinks.link)
                                                    )
                                                )
                                            }, horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                painter = painterResource(id = R.drawable.link_solid),
                                                contentDescription = "Link",
                                                modifier = Modifier
                                                    .height(25.dp),
                                                contentScale = ContentScale.Fit,
                                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("Link")
                                        }
                                    }

                                    if (!uploader.socialMediaLinks.other.isNullOrEmpty()) {
                                        Column {
                                            uploader.socialMediaLinks.other.forEach { otherLink ->
                                                if (otherLink.link != null && otherLink.link != "") {
                                                    Row(
                                                        modifier = Modifier
                                                            .padding(vertical = 10.dp)
                                                            .clickable {
                                                                context.startActivity(
                                                                    Intent(
                                                                        Intent.ACTION_VIEW,
                                                                        Uri.parse(otherLink.link)
                                                                    )
                                                                )
                                                            },
                                                        horizontalArrangement = Arrangement.Start,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Image(
                                                            painter = painterResource(id = R.drawable.link_solid),
                                                            contentDescription = otherLink.title,
                                                            modifier = Modifier
                                                                .height(25.dp),
                                                            contentScale = ContentScale.Fit,
                                                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                                                        )
                                                        Spacer(modifier = Modifier.width(12.dp))
                                                        otherLink.title?.let { it1 -> Text(it1) }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}