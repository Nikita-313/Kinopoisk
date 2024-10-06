package com.cinetech.ui.utils

import java.util.Locale

fun Double.format(scale: Int) = "%.${scale}f".format(Locale.US,this)