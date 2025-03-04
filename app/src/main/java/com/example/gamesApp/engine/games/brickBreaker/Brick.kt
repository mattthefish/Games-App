package com.example.gamesApp.engine.games.brickBreaker

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.util.UUID

class Brick(var offset: Offset = Offset(200f, 200f)) {

    val id: UUID = UUID.randomUUID()
    val height: Float = 100f
    val width: Float = 300f

    var bounds: Rect = Rect.Zero
}