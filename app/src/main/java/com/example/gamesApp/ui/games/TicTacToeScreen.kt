package com.example.gamesApp.ui.games

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamesApp.R
import com.example.gamesApp.engine.games.TicTacToeViewModel
import com.example.gamesApp.engine.games.TicTacToeViewModel.GameState
import com.example.gamesApp.engine.games.TicTacToeViewModel.Turn
import com.example.gamesApp.ui.theme.PlayerBlue
import com.example.gamesApp.ui.theme.PlayerOrange
import com.example.gamesApp.ui.utils.collectAsStateRepeatedly
import com.example.gamesApp.ui.utils.conditional
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun TicTacToeScreen(
    navigator: DestinationsNavigator
){
    val viewModel = viewModel<TicTacToeViewModel>()
    val state by viewModel.state.collectAsStateRepeatedly()

    TicTacToeScreenContent(
        onBackClicked = { navigator.navigateUp() },
        onCellClicked = { index -> viewModel.tilePressed(index) },
        state = state
    )
}

@Composable
fun TicTacToeScreenContent(
    onBackClicked: () -> Unit,
    onCellClicked: (Int) -> Unit,
    state: GameState
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (state.playerTurn == Turn.O) PlayerOrange else PlayerBlue)
    ) {
       TopNavBar(onBackClicked = onBackClicked)

        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TurnIndicator(
                state = state
            )
            TicTacToeGrid(
                state = state,
                onCellClicked = onCellClicked
            )
        }
    }
}

@Composable
fun TicTacToeGrid(
    state: GameState,
    onCellClicked: (Int) -> Unit
){
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        (0 until 3).forEach { row ->
            Row {
                (0 until 3).forEach { col ->
                    val cellIndex = row * 3 + col
                    val cellValue = state.board[cellIndex]

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                            .background(
                                MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .conditional(
                                predicate = (cellValue == null),
                                positive = {
                                    clickable(
                                        onClick = { onCellClicked(cellIndex) }
                                    )
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        cellValue?.let {
                            Text(
                                text = cellValue.name,
                                style = MaterialTheme.typography.displayLarge,
                                color = cellValue.color
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TurnIndicator(
    state: GameState
) {
    val animationSpec: AnimationSpec<Float> = tween(durationMillis = 400, easing = FastOutSlowInEasing)

    val startWeight by animateFloatAsState(
        targetValue = if(state.playerTurn == Turn.O) 1f else 0.00001f,
        animationSpec = animationSpec,
        label = "Underline start spacer weight"
    )
    val endWeight by animateFloatAsState(
        targetValue = if(state.playerTurn == Turn.O) 0.00001f else 1f,
        animationSpec = animationSpec,
        label = "Underline end spacer weight"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "X",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "O",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.secondary

        )
    }
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.weight(startWeight))
        Row(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 64.dp)
                .fillMaxWidth(0.5f)
                .height(8.dp)
                .background(MaterialTheme.colorScheme.secondary),
            content = {}
        )
        Spacer(modifier = Modifier.weight(endWeight))
    }
}

@Composable
fun TopNavBar(
    onBackClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp),
    ) {
        Button(
            modifier = Modifier.size(60.dp,30.dp),
            contentPadding = PaddingValues(6.dp),
            onClick = onBackClicked,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondary )
        ) {
            Icon(
                modifier = Modifier
                    .rotate(180f),
                painter = painterResource(id = R.drawable.right_arrow),
                contentDescription = "Go to menu",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

//PREVIEWS

@Preview(
    name = "TicTacToe Screen Blue"
)
@Composable
fun TicTacToeScreenBluePreview() {
    TicTacToeScreenContent(
        onBackClicked = {},
        onCellClicked = {},
        state = GameState()
    )
}

@Preview(
    name = "TicTacToe Screen Orange"
)
@Composable
fun TicTacToeScreenOrangePreview() {
    TicTacToeScreenContent(
        onBackClicked = {},
        onCellClicked = {},
        state = GameState(playerTurn = Turn.X)
    )
}