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
import androidx.compose.runtime.MutableState
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
import com.example.gamesApp.engine.games.brickBreaker.LaunchPad
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
        onBackClicked = { navigator.navigateUp() },
        onUpdate = { offset, bounds, x, y -> viewModel.onUpdate(offset, bounds, x, y) },
        initBallOffset = { x, y -> viewModel.initOffset(x, y)}
    )
}

@Composable
fun BrickBreakerScreenContent(
    state: GameState,
    onBackClicked: () -> Unit,
    onUpdate: (Offset, MutableState<Rect>, Float, Float) -> Unit,
    initBallOffset: (Float, Float) -> Unit
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
        initBallOffset(ballStartX, ballStartY)

        //Change location and check collisions
        LaunchedEffect(Unit){
            while (true) {
                onUpdate(launchPadOffset,launchPadBounds, parentWidthPx, parentHeightPx)
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
                    state.ball.offset.round()
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