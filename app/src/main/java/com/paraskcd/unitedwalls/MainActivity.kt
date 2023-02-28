package com.paraskcd.unitedwalls

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.paraskcd.unitedwalls.components.TopBar
import com.paraskcd.unitedwalls.ui.theme.UWallsTheme
import com.paraskcd.unitedwalls.viewmodel.WallsViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.paraskcd.unitedwalls.components.Drawer
import com.paraskcd.unitedwalls.screens.About
import com.paraskcd.unitedwalls.screens.Categories
import com.paraskcd.unitedwalls.screens.Home
import com.paraskcd.unitedwalls.screens.WallScreen
import com.paraskcd.unitedwalls.utils.Constants
import com.paraskcd.unitedwalls.viewmodel.CategoryViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val wallsViewModel: WallsViewModel = hiltViewModel()
            val categoryViewModel: CategoryViewModel = hiltViewModel()
            var isDrawerActive: Boolean by remember { mutableStateOf(false) }
            var isCategoryScreenActive: Boolean by remember { mutableStateOf(false) }
            var screenActive: Int by remember { mutableStateOf(0) }
            var categoryActive: String? by remember { mutableStateOf(null) }
            var wallScreenActive: Boolean by remember { mutableStateOf(false) }

            UWallsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Home(
                            openDrawer = { isDrawerActive = it },
                            isDrawerActive = isDrawerActive,
                            screenActive = screenActive,
                            wallsViewModel = wallsViewModel,
                            makeWallScreenActive = { wallScreenActive = it }
                        )
                        Categories(
                            openDrawer = { isDrawerActive = it },
                            isDrawerActive = isDrawerActive,
                            screenActive = screenActive,
                            categoryViewModel = categoryViewModel,
                            categoryActive = categoryActive,
                            makeCategoryScreenActive = { active, id ->
                                isCategoryScreenActive = active
                                categoryActive = id
                            }
                        )
                        About(
                            openDrawer = { isDrawerActive = it },
                            isDrawerActive = isDrawerActive,
                            screenActive = screenActive
                        )
                        TopBar(
                            screenActive = screenActive,
                            openDrawer = { isDrawerActive = it },
                            openScreen = { screenActive = it },
                            categoryViewModel = categoryViewModel,
                            categoryActive = categoryActive
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.primary
                                        )
                                    )
                                )
                        )
                        AnimatedVisibility(
                            visible = isDrawerActive,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xA5101010)))
                        }
                        Drawer(
                            isDrawerActive = isDrawerActive,
                            openDrawer = { isDrawerActive = it },
                            categoryViewModel = categoryViewModel,
                            screenActive = screenActive,
                            openScreen = { screenActive = it }
                        )
                        WallScreen(
                            wallScreenActive = wallScreenActive,
                            makeWallScreenActive = { wallScreenActive = it },
                            wallsViewModel = wallsViewModel
                        )
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                            AndroidView(modifier = Modifier.fillMaxWidth(), factory = { context ->
                                AdView(context).apply {
                                    // on below line specifying ad size
                                    setAdSize(AdSize.BANNER)
                                    // on below line specifying ad unit id
                                    // currently added a test ad unit id.
                                    adUnitId = if (BuildConfig.DEBUG) Constants.TEST_AD else Constants.PUBLIC_AD
                                    // calling load ad to load our ad.
                                    loadAd(AdRequest.Builder().build())
                                }
                            })
                        }
                    }
                }
            }
        }

        MobileAds.initialize(this)
    }
}