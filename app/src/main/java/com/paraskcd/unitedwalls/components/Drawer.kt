package com.paraskcd.unitedwalls.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paraskcd.unitedwalls.R
import com.paraskcd.unitedwalls.viewmodel.CategoryViewModel

@Composable
fun Drawer(
    isDrawerActive: Boolean,
    openDrawer: (Boolean) -> Unit,
    categoryViewModel: CategoryViewModel,
    screenActive: Int,
    openScreen: (Int) -> Unit
) {
    var pinnedSize: Dp by remember { mutableStateOf(0.dp) }
    val categories = categoryViewModel.categories.observeAsState().value
    BackHandler(enabled = isDrawerActive) {
        openDrawer(false)
    }
    val density = LocalDensity.current
    pinnedSize = when(categories?.size) {
        1 -> 95.dp
        2 -> 140.dp
        3 -> 190.dp
        else -> 235.dp
    }
    AnimatedVisibility(
        visible = isDrawerActive,
        enter = slideInHorizontally {
            with(density) { -40.dp.roundToPx() }
        } + expandHorizontally(
            expandFrom = Alignment.Start
        ) + fadeIn(
            initialAlpha = 0.3f
        ),
        exit = slideOutHorizontally()
                + shrinkHorizontally()
                + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(270.dp)
                .background(MaterialTheme.colorScheme.primary)
                .clickable(
                    onClick = {
                        if (isDrawerActive) {
                            openDrawer(false)
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ),
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.drawerbg),
                contentDescription = "DrawerBanner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
            DrawerItem(
                onClick = {
                    openDrawer(false)
                    openScreen(it)
                },
                icon = painterResource(id = R.drawable.home),
                iconDescription = "Home Icon",
                title = "Home",
                screenActive = screenActive == 0,
                screenIndex = 0
            )
            DrawerItem(
                onClick = {
                    openDrawer(false)
                    openScreen(it)
                },
                icon = painterResource(id = R.drawable.category),
                iconDescription = "Category Icon",
                title = "Categories",
                screenActive = screenActive == 1,
                screenIndex = 1
            )
            //TODO Pinning of Categories
//            Container(backgroundColor: MaterialTheme.colorScheme.Tertiary) {
//                Row(modifier = Modifier.padding(12.dp)) {
//                    Spacer(modifier = Modifier.width(30.dp))
//                    Box(modifier = Modifier
//                        .width(3.dp)
//                        .height(pinnedSize)
//                        .clip(CircleShape)
//                        .background(
//                            Red
//                        ))
//                    LazyColumn(modifier = Modifier
//                        .fillMaxWidth()
//                        .height(pinnedSize)
//                        .padding(start = 12.dp)) {
//                        item {
//                            Row(modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 12.dp, vertical = 6.dp),
//                                horizontalArrangement = Arrangement.Start,
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Image(
//                                    painter = painterResource(id = R.drawable.pin),
//                                    contentDescription = "Pin Icon",
//                                    modifier = Modifier
//                                        .width(30.dp)
//                                        .height(30.dp)
//                                )
//                                Spacer(modifier = Modifier.width(10.dp))
//                                Text(text = "Pinned")
//                            }
//                        }
//                        categories?.size?.let {
//                            items(it) { index ->
//                                val category = categories[index]
//                                Button(onClick = { /*TODO*/ }) {
//                                    Text(text = category.name)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
            DrawerItem(
                onClick = {
                    openDrawer(false)
                    openScreen(it)
                },
                icon = painterResource(id = R.drawable.heart),
                iconDescription = "About Icon",
                title = "Favourites",
                screenActive = screenActive == 2,
                screenIndex = 2
            )
            DrawerItem(
                onClick = {
                    openDrawer(false)
                    openScreen(it)
                },
                icon = painterResource(id = R.drawable.about),
                iconDescription = "About Icon",
                title = "About",
                screenActive = screenActive == 3,
                screenIndex = 3
            )
        }
    }
}

@Composable
fun DrawerItem(
    onClick: (Int) -> Unit,
    icon: Painter,
    iconDescription: String,
    title: String,
    screenActive: Boolean,
    screenIndex: Int
) {
    Button(
        onClick = {
            onClick(screenIndex)
        },
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (screenActive) MaterialTheme.colorScheme.tertiary else Color.Transparent)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = icon,
                contentDescription = iconDescription,
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(title)
        }
    }
}