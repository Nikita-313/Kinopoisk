package com.cinetech.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,

    primaryContainer  = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,

    secondary  = LightSecondary,
    onSecondary  = LightOnSecondary,

    secondaryContainer  = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,

    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightSurface,
    onSurface = LightOnSurface,

    error = LightError,
    onError = LightOnError,
)

private val LocalPaddings = staticCompositionLocalOf<Paddings> {
    error("CompositionLocal LocalDimensions not present")
}

private val LocalSpacers = staticCompositionLocalOf<Spacers> {
    error("CompositionLocal LocalSpacers not present")
}

val MaterialTheme.spacers: Spacers
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacers.current

val MaterialTheme.paddings: Paddings
    @Composable
    @ReadOnlyComposable
    get() = LocalPaddings.current

@Composable
fun KinopoiskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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
        typography = Typography,
    ) {
        CompositionLocalProvider(
            LocalPaddings provides Paddings,
            LocalSpacers provides Spacers
        ) {
            content()
        }
    }
}