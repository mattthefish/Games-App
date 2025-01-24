package com.example.gamesApp.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Float.toDp(): Dp {
    val density = LocalDensity.current
    return with(density) {
        this@toDp.toDp()
    }
}

fun Float.isBetween(
    upper: Float,
    lower: Float,
    inclusive: Boolean = true,
): Boolean {
    return if (inclusive){
        this in lower..upper
    } else {
        this > lower && this < upper
    }
}