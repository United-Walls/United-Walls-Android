package com.paraskcd.unitedwalls.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimaryBG,
    onPrimary = DarkText,
    secondary = DarkSecondaryBG,
    onSecondary = DarkSubtext0,
    tertiary = DarkTertiaryBG,
    onTertiary = DarkSubtext1,
    background = DarkTertiaryBG,
    surface = DarkSecondaryBG,
    onBackground = DarkText,
    onSurface = DarkText
)

private val LightColorScheme = lightColorScheme(
    primary = LightTertiaryBG,
    onPrimary = LightText,
    secondary = LightSecondaryBG,
    onSecondary = LightSubtext0,
    tertiary = LightPrimaryBG,
    onTertiary = LightSubtext1,
    background = LightPrimaryBG,
    surface = LightSecondaryBG,
    onBackground = LightText,
    onSurface = LightText
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun UWallsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.primary.toArgb()

            if (darkTheme) {
                WindowCompat.getInsetsController(window, view)
                    ?.isAppearanceLightStatusBars = false

                WindowCompat.getInsetsController(window, view)
                    ?.isAppearanceLightNavigationBars = false
            } else {
                WindowCompat.getInsetsController(window, view)
                    ?.isAppearanceLightStatusBars = true

                WindowCompat.getInsetsController(window, view)
                    ?.isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}