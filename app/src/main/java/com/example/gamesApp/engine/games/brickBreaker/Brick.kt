package com.example.gamesApp.engine.games.brickBreaker

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

class Brick(var offset: Offset = Offset(200f, 200f)) {

    val height: Float = 100f
    val width: Float = 300f

    var bounds: Rect = Rect.Zero
}