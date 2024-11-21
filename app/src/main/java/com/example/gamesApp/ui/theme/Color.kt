package com.example.gamesApp.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Black = Color(0xFF000000)
val Black40 = Color(0xFF111111)
val Black80 = Color(0xFF222222)

val White = Color(0xFFFFFFFF)
val White40 = Color(0xFFEEEEEE)
val White80 = Color(0xFFDDDDDD)

val PlayerOrange = Color(0xFFF17720)
val PlayerBlue = Color(0xFF0474BA)

val Grey = Color(0xFF808080)

val DarkColorScheme = darkColorScheme(
    primary = Black,
    onPrimary = White,
    secondary = Black40,
    onSecondary = White40,
    background = White
)

val LightColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    secondary = White40,
    onSecondary = Black40,
    background = Black
)
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
