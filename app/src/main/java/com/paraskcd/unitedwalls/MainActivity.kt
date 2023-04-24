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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.paraskcd.unitedwalls.components.Drawer
import com.paraskcd.unitedwalls.screens.*
import com.paraskcd.unitedwalls.utils.Constants
import com.paraskcd.unitedwalls.viewmodel.CategoryViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val app = context.applicationContext as UnitedWalls
            val wallsViewModel: WallsViewModel = hiltViewModel()
            val categoryViewModel: CategoryViewModel = hiltViewModel()
            val privacyPolicyAccepted = remember {
                mutableStateOf(app.preferences?.getBoolean("privacy-policy-accepted", false))
            }
            val category = categoryViewModel.categoryById.observeAsState().value
            val loadingCategory = categoryViewModel.loadingCategoryById.observeAsState().value
            var isDrawerActive: Boolean by remember { mutableStateOf(false) }
            var screenActive: Int by remember { mutableStateOf(0) }
            var categoryActive: String? by remember { mutableStateOf(null) }
            var wallOfDayScreenActive: Boolean by remember { mutableStateOf(false) }
            var mostLikedScreenActive: Boolean by remember { mutableStateOf(false) }
            var mostDownloadedScreenActive: Boolean by remember {
                mutableStateOf(false)
            }
            var wallScreenActive: Boolean by remember { mutableStateOf(false) }
            var categoryWallScreenActive: Boolean by remember { mutableStateOf(false) }
            var categoryWallIndex: Int by remember { mutableStateOf(0) }
            var favouriteWallScreenActive: Boolean by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(key1 = screenActive == 4) {
                if (categoryActive != null) {
                    categoryViewModel.getCategoryById(categoryId = categoryActive!!)
                }
            }

            LaunchedEffect(key1 = isDrawerActive == true) {
                wallsViewModel.getPopulatedFavouriteWalls()
            }

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
                        if (privacyPolicyAccepted.value == true) {
                            Home(
                                openDrawer = { isDrawerActive = it },
                                isDrawerActive = isDrawerActive,
                                screenActive = screenActive,
                                wallsViewModel = wallsViewModel,
                                makeWallScreenActive = { wallScreenActive = it },
                                makeWallOfDayScreenActive = { wallOfDayScreenActive = it },
                                makeMostLikedWallScreenActive = { mostLikedScreenActive = it },
                                makeMostDownloadedWallScreenActive = { mostDownloadedScreenActive = it },
                                makeMostLikedActive = {
                                    screenActive = 8
                                },
                                makeMostDownloadedActive = {
                                    screenActive = 7
                                }
                            )
                            FavouriteWalls(
                                openDrawer = { isDrawerActive = it },
                                isDrawerActive = isDrawerActive,
                                screenActive = screenActive,
                                wallsViewModel = wallsViewModel,
                                makeFavouriteWallsScreenActive = { favouriteWallScreenActive = it }
                            )
                            Categories(
                                openDrawer = { isDrawerActive = it },
                                isDrawerActive = isDrawerActive,
                                screenActive = screenActive,
                                categoryViewModel = categoryViewModel,
                                makeCategoryScreenActive = {
                                    categoryViewModel.clearCategoryById()
                                    categoryActive = it
                                    screenActive = 4
                                }
                            )
                            About(
                                openDrawer = { isDrawerActive = it },
                                isDrawerActive = isDrawerActive,
                                screenActive = screenActive
                            )
                            CategoryScreen(
                                openDrawer = { isDrawerActive = it },
                                isDrawerActive = isDrawerActive,
                                screenActive = screenActive,
                                category = category,
                                loadingCategory = loadingCategory,
                                makeCategoryWallScreenActive = { flag, index ->
                                    categoryWallScreenActive = flag
                                    categoryWallIndex = index
                                }
                            )
                            MostLiked(
                                openDrawer = { isDrawerActive = it },
                                isDrawerActive = isDrawerActive,
                                screenActive = screenActive,
                                wallsViewModel = wallsViewModel,
                                makeMostLikedWallScreenActive = { mostLikedScreenActive = it }
                            )
                            MostDownloaded(
                                openDrawer = { isDrawerActive = it },
                                isDrawerActive = isDrawerActive,
                                screenActive = screenActive,
                                wallsViewModel = wallsViewModel,
                                makeMostDownloadedWallScreenActive = { mostDownloadedScreenActive = it }
                            )
                            TopBar(
                                screenActive = screenActive,
                                openDrawer = { isDrawerActive = it },
                                openScreen = { screenActive = it }
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
                            Drawer(
                                isDrawerActive = isDrawerActive,
                                openDrawer = { isDrawerActive = it },
                                categoryViewModel = categoryViewModel,
                                screenActive = screenActive,
                                openScreen = { screenActive = it },
                                makeCategoryScreenActive = {
                                    categoryViewModel.clearCategoryById()
                                    categoryActive = it
                                    screenActive = 4
                                    categoryViewModel.getCategoryById(it)
                                },
                                wallsViewModel = wallsViewModel
                            )
                            WallOfDayScreen(
                                wallOfDayScreenActive = wallOfDayScreenActive,
                                makeWallOfDayScreenActive = { wallOfDayScreenActive = it },
                                wallsViewModel = wallsViewModel,
                                categoryViewModel = categoryViewModel
                            )
                            MostLikedWallScreen(
                                mostLikedWallScreen = mostLikedScreenActive,
                                makeMostLikedWallScreenActive = { mostLikedScreenActive = it },
                                wallsViewModel = wallsViewModel,
                                categoryViewModel = categoryViewModel
                            )
                            MostDownloadedWallScreen(
                                mostDownloadedWallScreenActive = mostDownloadedScreenActive,
                                makeMostDownloadedWallScreenActive = { mostDownloadedScreenActive = it },
                                wallsViewModel = wallsViewModel,
                                categoryViewModel = categoryViewModel
                            )
                            WallScreen(
                                wallScreenActive = wallScreenActive,
                                makeWallScreenActive = { wallScreenActive = it },
                                wallsViewModel = wallsViewModel,
                                categoryViewModel = categoryViewModel
                            )
                            CategoryWallScreen(
                                categoryWallScreenActive = categoryWallScreenActive,
                                makeCategoryWallScreenActive = { categoryWallScreenActive = it },
                                category = category,
                                categoryWallIndex = categoryWallIndex,
                                wallsViewModel = wallsViewModel
                            )
                            FavouriteWallScreen(
                                favouriteWallScreenActive = favouriteWallScreenActive,
                                makeFavouriteWallScreenActive = { favouriteWallScreenActive = it },
                                wallsViewModel = wallsViewModel,
                                categoryViewModel = categoryViewModel
                            )
                        } else {
                            PrivacyPolicy(acceptPrivacyPolicy = {
                                privacyPolicyAccepted.value = true
                                app.preferences?.edit()?.putBoolean("privacy-policy-accepted", true)?.apply()
                            })
                            TopBar(
                                screenActive = screenActive,
                                openDrawer = { isDrawerActive = it },
                                openScreen = { screenActive = it }
                            )
                        }
                    }
                }
            }
        }

        MobileAds.initialize(this)
    }
}