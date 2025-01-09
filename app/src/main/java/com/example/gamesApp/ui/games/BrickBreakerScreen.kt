package com.example.gamesApp.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.example.gamesApp.engine.games.brickBreaker.BrickBreakerViewModel
import com.example.gamesApp.engine.games.brickBreaker.BrickBreakerViewModel.GameState
import com.example.gamesApp.ui.utils.collectAsStateRepeatedly
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

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
                .clipToBounds(),
        ) {
            val parentWidthPx = constraints.maxWidth
            val parentHeightPx = constraints.maxHeight

            val startX = parentWidthPx - 100f
            val startY = parentHeightPx - state.launchPad.height - 100

            var offset by remember {
                mutableStateOf(
                    Offset(startX, startY)
                )
            }

            Surface(
                modifier = Modifier
                    .size(
                        100f.dp,
                        30f.dp
                    )
                    .offset {
                        offset.round()
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, _, _ ->
                            val summed = offset + pan
                            offset = Offset(
                                x = summed.x.coerceIn(
                                    0f,
                                    parentWidthPx - 100f
                                ),
                                y = startY,
                            )
                        }
                    },
                shape = RectangleShape,
                color = Color.White,
            ) {
                // Content of the draggable point
            }
        }
    }

}