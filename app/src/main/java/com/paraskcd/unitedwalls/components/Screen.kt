package com.paraskcd.unitedwalls.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Screen(openDrawer: (Boolean) -> Unit, isDrawerActive: Boolean, screenActive: Int, screenIndex: Int, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = screenIndex == screenActive,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (delta < 0) {
                            openDrawer(false)
                        } else {
                            if (delta > 10) {
                                openDrawer(true)
                            }
                        }
                    }
                )
                .clickable(
                    onClick = {
                        if (isDrawerActive) {
                            openDrawer(false)
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                )
        ) {
            content()
        }
    }
}