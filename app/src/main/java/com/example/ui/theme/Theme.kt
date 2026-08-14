package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val DarkColorScheme = darkColorScheme(
    primary = TavanaCyan,
    onPrimary = Color.White,
    primaryContainer = TavanaNavyLight,
    onPrimaryContainer = TavanaCyan,
    secondary = TavanaGold,
    onSecondary = Color.Black,
    tertiary = TavanaEmerald,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = CardDark
)

private val LightColorScheme = lightColorScheme(
    primary = TavanaCyanDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = TavanaNavy,
    secondary = TavanaGold,
    onSecondary = Color.Black,
    tertiary = TavanaEmerald,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = CardLightBorder
)

private val HighContrastColorScheme = darkColorScheme(
    primary = Color(0xFF00FFFF), // Pure Electric Cyan
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF003366),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFFFFD700), // Pure Gold Yellow
    onSecondary = Color.Black,
    tertiary = Color(0xFF00FF7F), // Bright Spring Green
    background = Color(0xFF000000), // Pure Black Background for maximum contrast
    surface = Color(0xFF121212),
    onBackground = Color(0xFFFFFFFF), // Pure White Text
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF1E1E1E)
)

@Composable
fun TavanaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    lang: String = "fa",
    highContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        highContrast -> HighContrastColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val layoutDir = if (lang.lowercase() == "en") LayoutDirection.Ltr else LayoutDirection.Rtl

    CompositionLocalProvider(LocalLayoutDirection provides layoutDir) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
