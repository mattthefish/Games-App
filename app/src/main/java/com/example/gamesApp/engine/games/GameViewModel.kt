package com.example.gamesApp.engine.games

import androidx.lifecycle.ViewModel
import com.example.gamesApp.R
import com.example.gamesApp.ui.destinations.DirectionDestination
import com.example.gamesApp.ui.destinations.HomeScreenDestination

abstract class GameViewModel: ViewModel() {
    abstract val name: String
    abstract val imageId: Int
    abstract val destination: DirectionDestination
}

class TicTacToePreview() : GameViewModel() {
    override val name: String = "Tic Tac Toe"
    override val imageId: Int = R.drawable.ic_launcher_foreground
    override val destination: DirectionDestination = HomeScreenDestination
}

class BrickBreakerPreview() : GameViewModel() {
    override val name: String = "Brick Breaker"
    override val imageId: Int = R.drawable.ic_brick_breaker
    override val destination: DirectionDestination = HomeScreenDestination
}