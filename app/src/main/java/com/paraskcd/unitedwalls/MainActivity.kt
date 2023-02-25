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
import androidx.compose.foundation.lazy.LazyColumn
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
import com.paraskcd.unitedwalls.components.Drawer
import com.paraskcd.unitedwalls.screens.About
import com.paraskcd.unitedwalls.viewmodel.CategoryViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val wallsViewModel: WallsViewModel = hiltViewModel()
            val categoryViewModel: CategoryViewModel = hiltViewModel()
            var isDrawerActive: Boolean by remember { mutableStateOf(false) }
            var screenActive: Int by remember { mutableStateOf(0) }

            UWallsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.primary
                                    )
                                )
                            )
                    ) {
                        if (screenActive == 3) {
                            About(openDrawer = { isDrawerActive = it }, isDrawerActive = isDrawerActive)
                        }
                        TopBar(
                            openDrawer = { isDrawerActive = it }
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
                    }
                }
            }
        }
    }
}