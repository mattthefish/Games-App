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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.gamesApp.ui.theme.PlayerBlue
import com.example.gamesApp.ui.theme.PlayerOrange
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@Destination
@Composable
fun TicTacToeScreen(
    navigator: DestinationsNavigator
){
    val viewModel = viewModel<TicTacToeViewModel>()
    val state = viewModel.gameState

    TicTacToeScreenContent(
        onBackClicked = { navigator.navigateUp() },
        onCellClicked = { index -> viewModel.tilePressed(index) },
        state = state
    )
}

@Composable
fun TicTacToeScreenContent(
    onBackClicked: () -> Unit,
    playerColor: MutableState<Boolean> = remember{ mutableStateOf(true) },
    onCellClicked: (Int) -> Unit,
    state: GameState
){
    val animationSpec: AnimationSpec<Float> = tween(durationMillis = 400, easing = FastOutSlowInEasing)

    val startWeight by animateFloatAsState(
        targetValue = if(playerColor.value) 1f else 0.00001f,
        animationSpec = animationSpec,
        label = "Underline start spacer weight"
    )
    val endWeight by animateFloatAsState(
        targetValue = if(playerColor.value) 0.00001f else 1f,
        animationSpec = animationSpec,
        label = "Underline end spacer weight"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (playerColor.value) PlayerOrange else PlayerBlue)
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

        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                                    .clickable( //TODO add conditional
                                        onClick = { onCellClicked(cellIndex) }
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
    }
}

@Composable
fun TicTacToeButton(icon: Int){

    var enabled by remember { mutableStateOf(true) }

    LaunchedEffect(enabled) {
        if (enabled) return@LaunchedEffect
        else delay(100L)
        enabled = true
    }

    Button(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        onClick = {enabled = false},
        enabled = enabled
    ) {
        Icon(
            painter = painterResource(id = icon) ,
            contentDescription = "Go to menu",
            tint = MaterialTheme.colorScheme.onSecondary
        )
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
        state = GameState()
    )
}
