package com.example.gamesApp.engine.games.brickBreaker

import androidx.compose.ui.geometry.Offset
import com.example.gamesApp.R
import com.example.gamesApp.engine.games.GameViewModel
import com.example.gamesApp.ui.destinations.BrickBreakerScreenDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class BrickBreakerViewModel: GameViewModel() {
    override val name: String = "Brick Breaker"
    override val imageId: Int = R.drawable.ic_brick_breaker
    override val destination: BrickBreakerScreenDestination = BrickBreakerScreenDestination

    private var internalState = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = internalState.asStateFlow()

    fun destroyBrick(id: UUID) {
        val newDestroyedBricks: Set<UUID> = state.value.destroyedBricks.plus(id)
        internalState.value = state.value.copy(
                destroyedBricks = newDestroyedBricks
        )
    }

    data class GameState(
        val paused: Boolean = false,
        val bricks: Set<Brick> = setOf(Brick(), Brick(Offset(600f, 600f)), Brick(Offset(400f, 1500f))),
        val destroyedBricks: Set<UUID> = setOf()
    ) {
        val launchPad = LaunchPad()
        val ball = Ball()
    }
}

