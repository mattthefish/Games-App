package com.example.gamesApp.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamesApp.R
import com.example.gamesApp.engine.games.TicTacToeViewModel
import com.example.gamesApp.engine.games.TicTacToeViewModel.Players
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

    TicTacToeScreenContent(
        onBackClicked = { navigator.popBackStack() },
        onCellClicked = { },
        playerColor = PlayerBlue //TODO dynamic
    )
}

@Composable
fun TicTacToeScreenContent(
    onBackClicked: () -> Unit,
    playerColor: Color,
    onCellClicked: (Players) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(playerColor)
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
                onClick = { onBackClicked },
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
            modifier = Modifier.padding(32.dp)
        ) {

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
                            val cellValue = "X"

                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp)
                                    .background(
                                        MaterialTheme.colorScheme.background,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
//                                        if (cellValue.isEmpty()) {
//                                            when (/*current player*/) {
//                                                X -> {
//                                                    //place piece and change turn
//                                                }
//                                                O -> {
//                                                    //place piece and change turn
//                                                }
//                                            }
//                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cellValue,
                                    style = MaterialTheme.typography.displayLarge,
                                    color = playerColor
                                )
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
        playerColor = PlayerBlue
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
        playerColor = PlayerOrange
    )
}
