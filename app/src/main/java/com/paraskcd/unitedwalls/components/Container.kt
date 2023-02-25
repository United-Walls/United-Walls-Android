package com.paraskcd.unitedwalls.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Container(backgroundColor: Color, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp).fillMaxWidth().clip(shape = RoundedCornerShape(12.dp)).shadow(elevation = 12.dp).background(color = backgroundColor),
    ) {
        content()
    }
}