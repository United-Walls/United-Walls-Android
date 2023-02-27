package com.paraskcd.unitedwalls.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.unitedwalls.components.Container
import com.paraskcd.unitedwalls.components.NetworkImage
import com.paraskcd.unitedwalls.components.Screen

@Composable
fun About(openDrawer: (Boolean) -> Unit, isDrawerActive: Boolean, screenActive: Int) {
    val context = LocalContext.current

    Screen(
        openDrawer = { openDrawer(it) },
        isDrawerActive = isDrawerActive,
        screenActive = screenActive,
        screenIndex = 3
    ) {
        LazyColumn {
            item { 
                Spacer(modifier = Modifier.height(84.dp))
            }
            item {
                Container(backgroundColor = MaterialTheme.colorScheme.primary) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "About", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "United Walls is an App created with love by the United Setups community in Telegram. All Wallpapers are provided by the community to share them with you. Enjoy your devices with our handcrafted beautiful and artistic Designs.")
                    }
                }
            }
            item {
                Container(backgroundColor = MaterialTheme.colorScheme.primary) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Links", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "• Telegram", modifier = Modifier.padding(6.dp).clip(CircleShape).clickable { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/unitedsetups"))) })
                        Text(text = "• GitHub", modifier = Modifier.padding(6.dp).clip(CircleShape).clickable { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/United-Walls"))) })
                    }
                }
            }
            item {
                Container(backgroundColor = MaterialTheme.colorScheme.primary) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Credits", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clip(CircleShape)
                                .clickable {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://paraskcd.com")
                                        )
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            NetworkImage(imageURL = "https://github.com/paraskcd1315.png", imageDescription = "ParasKCD Profile Image", size = 64.dp, shape = CircleShape)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "Paras KCD", fontSize = 18.sp, fontWeight = FontWeight(700))
                                Text(text = "Android, iOS, Bot", fontSize = 12.sp, fontWeight = FontWeight(400), color = MaterialTheme.colorScheme.onSecondary)
                                Text(text = "and Server Developer", fontSize = 12.sp, fontWeight = FontWeight(400), color = MaterialTheme.colorScheme.onSecondary)
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clip(CircleShape)
                                .clickable {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://t.me/unitedsetups")
                                        )
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Image(
                                painter = painterResource(id = com.paraskcd.unitedwalls.R.drawable.uslogo),
                                contentDescription = "United Setups Logo",
                                modifier = Modifier.padding(10.dp).clip(CircleShape).size(64.dp).background(MaterialTheme.colorScheme.tertiary)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "The Community", fontSize = 18.sp, fontWeight = FontWeight(700))
                                Text(text = "Wallpaper Uploaders", fontSize = 12.sp, fontWeight = FontWeight(400), color = MaterialTheme.colorScheme.onSecondary)
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(84.dp))
            }
        }
    }
}