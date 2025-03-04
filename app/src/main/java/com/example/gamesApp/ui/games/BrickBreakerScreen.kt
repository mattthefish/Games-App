package com.example.gamesApp.ui.games

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.round
import com.example.gamesApp.engine.games.brickBreaker.BrickBreakerViewModel
import com.example.gamesApp.engine.games.brickBreaker.BrickBreakerViewModel.GameState
import com.example.gamesApp.ui.utils.collectAsStateRepeatedly
import com.example.gamesApp.ui.utils.toDp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import java.util.UUID

@Destination
@Composable
fun BrickBreakerScreen(
    navigator: DestinationsNavigator
){
    val viewModel = BrickBreakerViewModel()
    val state by viewModel.state.collectAsStateRepeatedly()

    BrickBreakerScreenContent(
        state = state,
        onBackClicked = { navigator.navigateUp() },
        onBrickDestroyed = { viewModel.destroyBrick(it)}
    )
}

@Composable
fun BrickBreakerScreenContent(
    state: GameState,
    onBackClicked: () -> Unit,
    onBrickDestroyed: (UUID) -> Unit
) {
//    GameTopNavBar(onBackClicked = onBackClicked)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Black)
    ) {
        val parentWidthPx = constraints.maxWidth.toFloat()
        val parentHeightPx = constraints.maxHeight.toFloat()

        val launchPadStartX = parentWidthPx / 2  - state.launchPad.width / 2
        val launchPadStartY = parentHeightPx - 200f

        val launchPadBounds = remember { mutableStateOf(Rect.Zero) }

        var launchPadOffset by remember {
            mutableStateOf(
                Offset(launchPadStartX, launchPadStartY)
            )
        }

        val ballStartX = parentWidthPx / 2  - state.launchPad.width / 2
        val ballStartY = parentHeightPx - 500f

        var ballOffset by remember {
            mutableStateOf(
                Offset(ballStartX, ballStartY)
            )
        }

        //Change location and check collisions
        LaunchedEffect(Unit) {
            while (true) {
                ballOffset = Offset(
                    ballOffset.x + state.ball.velocity.x,
                    ballOffset.y + state.ball.velocity.y
                )
                state.ball.checkWallCollision(parentWidthPx, parentHeightPx, ballOffset)

                // check launch pad collision
                val newOffset = state.ball.checkRectangleCollision(
                    ballOffset = ballOffset,
                    rectBounds = launchPadBounds.value,
                    isBrick = false
                )

                // check launchpad rebound angle
                if(newOffset != null) {
                    state.ball.angledVelocity(
                        ballOffset = ballOffset,
                        launchPadOffset = launchPadOffset,
                        launchPadWidth = state.launchPad.width
                    )
                }

                // check brick collision
                state.bricks
                    .filter { it.id !in state.destroyedBricks }
                    .forEach {
                        if(state.ball.checkRectangleCollision(
                                ballOffset = ballOffset,
                                rectBounds = it.bounds,
                                isBrick = true
                        ) == null) {
                            // no op
                        } else { // if collision happens
                            onBrickDestroyed(it.id)
                        }
                    }

                delay(10L)
            }
        }

        //draw bricks
        state.bricks
         .forEach { brick ->
             AnimatedVisibility(
                 visible = brick.id !in state.destroyedBricks
             ) {
                 Surface(
                     modifier = Modifier
                         .size(
                             brick.width.toDp(),
                             brick.height.toDp()
                         )
                         .offset {
                             brick.offset.round()
                         }
                         .onGloballyPositioned { coordinates ->
                             brick.bounds = coordinates.boundsInRoot()
                         },
                     shape = RectangleShape,
                     color = Color.White,
                     content = { }
                 )
             }
         }


        //draw ball
        Surface(
            modifier = Modifier
                .size(state.ball.diameter.toDp())
                .offset {
                    ballOffset.round()
                }
            ,
            shape = CircleShape,
            color = Color.White,
            content = {  }
        )

        //draw launch pad
        Surface(
            modifier = Modifier
                .size(
                    state.launchPad.width.toDp(),
                    state.launchPad.height.toDp()
                )
                .offset {
                    launchPadOffset.round()
                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, _, _ ->
                        val newOffset = launchPadOffset + pan
                        launchPadOffset = Offset(
                            x = newOffset.x.coerceIn(
                                0f,
                                parentWidthPx - state.launchPad.width
                            ),
                            y = launchPadStartY,
                        )
                    }
                }
                .onGloballyPositioned { coordinates ->
                    launchPadBounds.value = coordinates.boundsInRoot()
                },
            shape = RectangleShape,
            color = Color.White,
            content = {  }
        )
    }
}