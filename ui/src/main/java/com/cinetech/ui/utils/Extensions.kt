package com.cinetech.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import java.util.Locale

fun Double.format(scale: Int) = "%.${scale}f".format(Locale.US,this)


inline val Float.px: Dp
    @Composable get() = with(LocalDensity.current) { this@px.toDp() }

inline val Dp.toPx: Float
    @Composable get() = with(LocalDensity.current) { this@toPx.toPx() }