package com.example.gamesApp.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.round
import com.example.gamesApp.engine.games.brickBreaker.BrickBreakerViewModel
import com.example.gamesApp.engine.games.brickBreaker.BrickBreakerViewModel.GameState
import com.example.gamesApp.ui.utils.collectAsStateRepeatedly
import com.example.gamesApp.ui.utils.toDp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@Destination
@Composable
fun BrickBreakerScreen(
    navigator: DestinationsNavigator
){
    val viewModel = BrickBreakerViewModel()
    val state by viewModel.state.collectAsStateRepeatedly()

    BrickBreakerScreenContent(
        state = state,
        onBackClicked = { navigator.navigateUp() }
    )
}

@Composable
fun BrickBreakerScreenContent(
    state: GameState,
    onBackClicked: () -> Unit
) {
//    GameTopNavBar(onBackClicked = onBackClicked)

    Column {
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

            var launchPadOffset by remember {
                mutableStateOf(
                    Offset(launchPadStartX, launchPadStartY)
                )
            }

            val ballStartX = parentWidthPx / 2  - state.launchPad.width / 2
            val ballStartY = parentHeightPx - 200f

            var ballOffset by remember {
                mutableStateOf(
                    Offset(ballStartX, ballStartY)
                )
            }

            LaunchedEffect(Unit) {
                while (true) {
                    ballOffset = Offset(
                        ballOffset.x + state.ball.velocity.x,
                        ballOffset.y + state.ball.velocity.y
                    )
                    state.ball.checkWallCollision(parentWidthPx, parentHeightPx, ballOffset)
                    state.ball.checkLaunchPadCollision(
                        ballOffset = ballOffset,
                        launchPad = state.launchPad,
                        launchPadOffset = launchPadOffset
                    )
                    delay(10L)
                }
            }

            Surface(
                modifier = Modifier
                    .size(state.ball.diameter.toDp())
                    .offset{
                        ballOffset.round()
                    }
                ,
                shape = CircleShape,
                color = Color.White,
                content = {  }
            )

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
                            val newOffsetX = launchPadOffset + pan
                            launchPadOffset = Offset(
                                x = newOffsetX.x.coerceIn(
                                    0f,
                                    parentWidthPx - state.launchPad.width
                                ),
                                y = launchPadStartY,
                            )
                        }
                    },
                shape = RectangleShape,
                color = Color.White,
                content = {  }
            )
        }
    }
}