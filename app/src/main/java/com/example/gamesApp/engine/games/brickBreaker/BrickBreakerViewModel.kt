package com.example.gamesApp.engine.games.brickBreaker

import androidx.compose.ui.geometry.Offset
import com.example.gamesApp.R
import com.example.gamesApp.engine.games.GameViewModel
import com.example.gamesApp.ui.destinations.BrickBreakerScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BrickBreakerViewModel: GameViewModel() {
    override val name: String = "Brick Breaker"
    override val imageId: Int = R.drawable.ic_brick_breaker
    override val destination: BrickBreakerScreenDestination = BrickBreakerScreenDestination

    private val internalState = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = internalState

    data class GameState(
        var paused: Boolean = false,
        var remainingBricks: List<Brick> = listOf(Brick(), Brick(Offset(600f, 600f)), Brick(Offset(400f, 1500f)))
    ) {
        val launchPad = LaunchPad()
        val ball = Ball()
    }
}

