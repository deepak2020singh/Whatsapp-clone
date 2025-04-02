package com.example.twitter.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00AF5A),       // WhatsApp green for buttons, etc.
    onPrimary = Color.White,           // Text/icon color on primary
    surface = Color(0xFF121212),       // Dark mode background
    onSurface = Color.White,           // Text/icon color on surface
    surfaceVariant = Color(0xFF2A2A2A), // Dark TextField background
    onSurfaceVariant = Color.White,    // Text on TextField background
    secondary = Color(0xFFB0B0B0),     // Lighter gray for secondary text
    outline = Color(0xFF888888)        // Dark mode borders/indicators
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00AF5A),       // WhatsApp green for buttons, etc.
    onPrimary = Color.White,           // Text/icon color on primary
    surface = Color.White,             // Light mode background
    onSurface = Color.Black,           // Text/icon color on surface
    surfaceVariant = Color(0xFFF0F0F0), // Light TextField background
    onSurfaceVariant = Color.Black,    // Text on TextField background
    secondary = Color.Gray,            // Secondary text (e.g., hints)
    outline = Color.Gray               // Borders, unfocused indicators
)

@Composable
fun TwitterCloneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography, // Use default typography or define your own
        content = content
    )
}