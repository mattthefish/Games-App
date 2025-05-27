package com.example.gamesApp.engine.games.brickBreaker

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Velocity
import com.example.gamesApp.R
import com.example.gamesApp.engine.games.GameViewModel
import com.example.gamesApp.ui.destinations.BrickBreakerScreenDestination
import com.example.gamesApp.ui.utils.isBetween
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

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

    private fun checkLaunchPadCollision(launchPadOffset: Offset, launchPadBounds: MutableState<Rect>) {
        val hasCollided = checkRectangleCollision(
            rectBounds = launchPadBounds.value,
            isBrick = false
        )
        if(hasCollided) {
            angledVelocity(
                launchPadOffset = launchPadOffset,
                launchPadWidth = state.value.launchPad.width
            )
        }
    }

    private fun angledVelocity(
        launchPadOffset: Offset,
        launchPadWidth: Float
    ) {
        val offset = state.value.ballOffset
        val velocity = state.value.ballVelocity
        val diameter = state.value.ball.diameter
        val ballCenter = offset.x + diameter/2
        val launchPadCenter = launchPadOffset.x + launchPadWidth/2

        if (ballCenter.isBetween(launchPadOffset.x + launchPadWidth, launchPadOffset.x)) {
            val relativeImpactX = (ballCenter - launchPadCenter) / (launchPadWidth / 2)

            val clampedImpactX = relativeImpactX.coerceIn(-1f, 1f)

            val exitAngle = Math.PI / 4 + (clampedImpactX + 1) * (Math.PI / 4)

            // Compute new velocity components
            val speed = sqrt(velocity.x.pow(2) + velocity.y.pow(2)) * -1f
            val vx = speed * cos(exitAngle).toFloat()
            val vy = speed * sin(exitAngle).toFloat()

            setVelocityAndOffset(
                velocity = Velocity(vx, vy)
            )
        }
    }


    private fun checkWallCollision(
        screenWidth: Float,
        screenHeight: Float,
    ): Boolean {
        val offset = state.value.ballOffset
        val velocity = state.value.ballVelocity
        val diameter = state.value.ball.diameter
        var hasCollided = false
        if (offset.x + diameter >= screenWidth
            || offset.x <= 0
        ) {
            setVelocityAndOffset(
                velocity = Velocity(
                    -velocity.x,
                    velocity.y
                )
            )

            hasCollided = if (offset.x <= 0 ) {
                setVelocityAndOffset(offset = Offset(0f, offset.y))
            } else {
                setVelocityAndOffset(offset = Offset(screenWidth - diameter, offset.y))
            }
        } else if (offset.y + diameter >= screenHeight
            || offset.y <= 0
        ) {
            setVelocityAndOffset(
                velocity = Velocity(
                    velocity.x,
                    -velocity.y
                )
            )

            hasCollided = if (offset.y <= 0 ) {
                setVelocityAndOffset(offset =  Offset(offset.x, 0f))
            } else {
                setVelocityAndOffset(offset = Offset(offset.x, screenHeight - diameter))
            }
        }
        return hasCollided
    }


    // returns false if no collision detected
    private fun checkRectangleCollision(
        rectBounds: Rect,
        isBrick: Boolean
    ): Boolean {
        val ballRadius = state.value.ball.diameter / 2
        val ballX = state.value.ballOffset.x + ballRadius
        val ballY = state.value.ballOffset.y + ballRadius

        // Ball-Rectangle Collision Detection
        val ballLeft = ballX - ballRadius
        val ballRight = ballX + ballRadius
        val ballTop = ballY - ballRadius
        val ballBottom = ballY + ballRadius

        val rectLeft = rectBounds.left
        val rectRight = rectBounds.right
        val rectTop = rectBounds.top
        val rectBottom = rectBounds.bottom

        val ballIntersectsRectHorizontally = ballRight > rectLeft && ballLeft < rectRight
        val ballIntersectsRectVertically = ballBottom > rectTop && ballTop < rectBottom

        return if (ballIntersectsRectHorizontally &&
            ballBottom <= rectBounds.center.y &&
            ballBottom >= rectTop &&
            state.value.ballVelocity.y > 0
        ) {
            // Ball hits the top of the rectangle
            setVelocityAndOffset(
                velocity = Velocity(
                    state.value.ballVelocity.x,
                    -state.value.ballVelocity.y
                ),
                offset =  Offset(state.value.ballOffset.x, rectTop - ballRadius*2)
            )

        } else if (ballIntersectsRectHorizontally &&
            ballTop >= rectBounds.center.y &&
            ballTop <= rectBottom &&
            state.value.ballVelocity.y < 0 &&
            isBrick
        ) {
            // Ball hits the bottom of the rectangle
            setVelocityAndOffset(
                velocity = Velocity(
                    state.value.ballVelocity.x,
                    -state.value.ballVelocity.y
                ),
                offset = Offset(state.value.ballOffset.x, rectBottom)
            )
        } else if (ballIntersectsRectVertically &&
            rectBounds.center.x > ballRight &&
            ballRight > rectLeft &&
            state.value.ballVelocity.x > 0
        ) {
            // Ball hits the left side of the rectangle
            setVelocityAndOffset(
                velocity = Velocity(
                    -state.value.ballVelocity.x,
                    state.value.ballVelocity.y
                ),
                offset = Offset(rectLeft - ballRadius*2 - 1, state.value.ballOffset.y),
            )
        } else if (ballIntersectsRectVertically &&
            rectBounds.center.x < ballLeft &&
            ballLeft < rectRight &&
            state.value.ballVelocity.x < 0
        ) {
            // Ball hits the right side of the rectangle
            setVelocityAndOffset(
                velocity = Velocity(
                    -state.value.ballVelocity.x,
                    state.value.ballVelocity.y
                ),
                offset = Offset(rectRight + 1f, state.value.ballOffset.y),
            )
        } else false
    }

    private fun updateBallOffset() {
        val currentState = state.value
        val newX = currentState.ballVelocity.x * 2f + currentState.ballOffset.x
        val newY = currentState.ballVelocity.y * 2f + currentState.ballOffset.y

        setVelocityAndOffset(
            offset = Offset(newX, newY)
        )
    }

    fun setInitOffset(x: Float, y: Float) {
        setVelocityAndOffset(
            offset = Offset(x, y)
        )
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

        checkWallCollision(parentWidthPx, parentHeightPx)

        // check brick collision
        state.value.bricks
            .filter { it.id !in state.value.destroyedBricks }
            .forEach {
                val hasCollided = checkRectangleCollision(
                    rectBounds = it.bounds,
                    isBrick = true
                )
                if(!hasCollided) {
                    // no op
                } else { // if collision happens
                    destroyBrick(it.id)
                }
            }
    }

    private fun setVelocityAndOffset(
        offset: Offset? = null,
        velocity: Velocity? = null
    ): Boolean {
        var changed = false
        offset?.let {
            internalState.value = state.value.copy(ballOffset = it)
            changed = true
        }
        velocity?.let {
            internalState.value = state.value.copy(ballVelocity = it)
            changed = true
        }
        if (changed) {
            println("internal state ball offset: ${internalState.value.ballOffset}")

        }
        return changed
    }

    suspend fun gameLoop(
        launchPadOffset: MutableState<Offset>,
        launchPadBounds: MutableState<Rect>,
        parentWidthPx: Float,
        parentHeightPx: Float
    ) {
        while (true) {
            onUpdate(launchPadOffset.value,launchPadBounds, parentWidthPx, parentHeightPx)
            delay(10L)
        }
    }

    data class GameState(
        val paused: Boolean = false,
        val bricks: Set<Brick> = setOf(Brick(), Brick(Offset(600f, 600f)), Brick(Offset(400f, 1500f))),
        val destroyedBricks: Set<UUID> = setOf(),
        val launchPad: LaunchPad = LaunchPad(),
        val ballVelocity: Velocity = Velocity(10f, -5f),
        val ballOffset: Offset = Offset(250f,500f),
        val ball: Ball = Ball()
    )
}