package com.example.plugd.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 1. Define your color palettes
private val LightColors = lightColorScheme(
    primary = Color(0xFFFF9800),
    onPrimary = Color.White,
    secondary = Color(0xFFD7D7D7),
    onSecondary = Color.Black,
    background = Color(0xFFF6F6F6),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFF9800),
    onPrimary = Color.Black,
    secondary = Color(0xFFD7D7D7),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)

// 2. Define your typography
private val AppTypography = Typography() // Use default Material3 typography or customize

// 3. Define your shapes
private val AppShapes = Shapes() // Default shapes

// 4. Create the theme composable
@Composable
fun PLUGDTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}