package com.example.gamesApp.engine.games

import com.example.gamesApp.R
import com.example.gamesApp.ui.destinations.DirectionDestination
import com.example.gamesApp.ui.destinations.HomeScreenDestination

interface Game {
    val name: String
    val imageId: Int
    val destination: DirectionDestination
}

class GamePreview() : Game {
    override val name: String = "Tic Tac Toe"
    override val imageId: Int = R.drawable.ic_launcher_foreground
    override val destination: DirectionDestination = HomeScreenDestination
}