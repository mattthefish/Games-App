package com.example.gamesApp.engine.games.brickBreaker

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Velocity
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

    private fun destroyBrick(id: UUID) {
        val newDestroyedBricks: Set<UUID> = state.value.destroyedBricks.plus(id)
        internalState.value = state.value.copy(
                destroyedBricks = newDestroyedBricks
        )
    }

    private fun updateBallOffset() {
        updateBall(
            offset = Offset(
                state.value.ball.offset.x + state.value.ball.velocity.x,
                state.value.ball.offset.y + state.value.ball.velocity.y
            ),
            oldBall = state.value.ball
        )
    }

    private fun updateBall(velocity: Velocity? = null, offset: Offset? = null, oldBall: Ball) {
        internalState.value = state.value.copy(
            ball = Ball(
                velocity = velocity ?: oldBall.velocity,
                offset = offset?: oldBall.offset
            )
        )
    }

    private fun replaceBall(newBall: Ball){
        internalState.value = state.value.copy(
            ball = newBall
        )
    }

    private fun checkLaunchPadCollision(launchPadOffset: Offset, launchPadBounds: MutableState<Rect>) {
        state.value.ball.checkRectangleCollision(
            rectBounds = launchPadBounds.value,
            isBrick = false
        )?.let {
            replaceBall(
                newBall = it
            )
            replaceBall(
                newBall = state.value.ball.angledVelocity(
                    launchPadOffset = launchPadOffset,
                    launchPadWidth = state.value.launchPad.width
                )
            )
        }

    }

    fun initOffset(x: Float, y: Float) {
        updateBall(offset = Offset(x, y), oldBall = state.value.ball)
    }

    fun onUpdate(
        launchPadOffset: Offset,
        launchPadBounds: MutableState<Rect>,
        parentWidthPx: Float,
        parentHeightPx: Float
    ) {
        updateBallOffset()

        checkLaunchPadCollision(
                launchPadOffset = launchPadOffset,
        launchPadBounds = launchPadBounds
        )

        state.value.ball.checkWallCollision(parentWidthPx, parentHeightPx)

        // check brick collision
        state.value.bricks
            .filter { it.id !in state.value.destroyedBricks }
            .forEach {
                if(state.value.ball.checkRectangleCollision(
                        rectBounds = it.bounds,
                        isBrick = true
                    ) == null) {
                    // no op
                } else { // if collision happens
                    destroyBrick(it.id)
                }
            }
    }

    data class GameState(
        val paused: Boolean = false,
        val bricks: Set<Brick> = setOf(Brick(), Brick(Offset(600f, 600f)), Brick(Offset(400f, 1500f))),
        val destroyedBricks: Set<UUID> = setOf(),
        val launchPad: LaunchPad = LaunchPad(),
        val ball: Ball = Ball(),
    )
}