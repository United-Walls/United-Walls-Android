package com.paraskcd.unitedwalls.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraskcd.unitedwalls.components.Container
import com.paraskcd.unitedwalls.components.NetworkImage
import com.paraskcd.unitedwalls.components.Screen

@Composable
fun About(openDrawer: (Boolean) -> Unit, isDrawerActive: Boolean) {
    val context = LocalContext.current

    Screen(openDrawer = { openDrawer(it) }, isDrawerActive = isDrawerActive) {
        LazyColumn {
            item { 
                Spacer(modifier = Modifier.height(64.dp))
            }
            item {
                Container(backgroundColor = MaterialTheme.colorScheme.primary) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "About", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "United Walls is an App created with love by the United Setups Team in Telegram. All Wallpapers are provided by the team to share them with you. Make your Wallpapers in your devices great again with our handcrafted beautiful and artistic Designs.")
                    }
                }
            }
            item {
                Container(backgroundColor = MaterialTheme.colorScheme.primary) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Links", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "• Telegram", modifier = Modifier.clickable { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/unitedsetups"))) })
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
                                .clickable {
                                    context.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://paraskcd.com")
                                        )
                                    )
                                },
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            NetworkImage(imageURL = "https://github.com/paraskcd1315.png", imageDescription = "ParasKCD Profile Image", size = 64.dp, shape = CircleShape)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "Paras KCD", fontSize = 18.sp, fontWeight = FontWeight(700))
                                Text(text = "Android, iOS, Bot and Server Developer", fontSize = 12.sp, fontWeight = FontWeight(400), color = MaterialTheme.colorScheme.onSecondary)
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            NetworkImage(imageURL = "https://pbs.twimg.com/profile_images/1620066360473649152/Nqf5sud__400x400.jpg", imageDescription = "NKnives Profile Image", size = 64.dp, shape = CircleShape)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "NKnives", fontSize = 18.sp, fontWeight = FontWeight(700))
                                Text(text = "Wallpaper Artist", fontSize = 12.sp, fontWeight = FontWeight(400), color = MaterialTheme.colorScheme.onSecondary)
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            NetworkImage(imageURL = "https://hndrk.blog/content/images/size/w150/2022/07/profile.webp", imageDescription = "HNDRK Profile Image", size = 64.dp, shape = CircleShape)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "HNDRK", fontSize = 18.sp, fontWeight = FontWeight(700))
                                Text(text = "Wallpaper Artist", fontSize = 12.sp, fontWeight = FontWeight(400), color = MaterialTheme.colorScheme.onSecondary)
                            }
                        }
                    }
                }
            }
        }
    }
}