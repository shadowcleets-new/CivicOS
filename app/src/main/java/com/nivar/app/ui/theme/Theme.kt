package com.nivar.app.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ===== Nivar Design System — Theme =====

private val DarkColorScheme = darkColorScheme(
    primary = NivarSky,
    onPrimary = NivarNavy,
    primaryContainer = NivarRoyal,
    onPrimaryContainer = NivarIce,
    secondary = NivarIce,
    onSecondary = NivarNavy,
    secondaryContainer = Color(0xFF1A3A5C),  // Darker blue for dark mode chips
    onSecondaryContainer = NivarIce,
    tertiary = NivarRoyal,
    onTertiary = PureWhite,
    background = NivarNavy,
    onBackground = NivarIce,
    surface = NivarSurface,
    onSurface = NivarIce,
    surfaceVariant = Color(0xFF1E3352),  // Card background in dark mode
    onSurfaceVariant = NivarSlate300,
    outline = NivarSlate500,
    outlineVariant = Color(0xFF2A4060),
    error = ErrorRed,
    onError = PureWhite,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

private val LightColorScheme = lightColorScheme(
    primary = NivarNavy,
    onPrimary = PureWhite,
    primaryContainer = NivarIce,
    onPrimaryContainer = NivarNavy,
    secondary = NivarRoyal,
    onSecondary = PureWhite,
    secondaryContainer = NivarIce,          // Chip backgrounds, highlights
    onSecondaryContainer = NivarNavy,
    tertiary = NivarSky,
    onTertiary = PureWhite,
    background = OffWhite,
    onBackground = NivarSlate900,
    surface = PureWhite,
    onSurface = NivarSlate900,
    surfaceVariant = NivarSlate50,          // Card backgrounds
    onSurfaceVariant = NivarSlate700,
    outline = NivarSlate300,
    outlineVariant = NivarSlate200,
    error = ErrorRed,
    onError = PureWhite,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

@Composable
fun NivarTheme(
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes,
        typography = Typography,
        content = content
    )
}
