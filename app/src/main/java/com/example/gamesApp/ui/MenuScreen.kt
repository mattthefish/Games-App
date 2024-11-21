package com.example.gamesApp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gamesApp.engine.games.GameViewModel
import com.example.gamesApp.engine.games.GamePreview
import com.example.gamesApp.ui.destinations.DirectionDestination
import com.example.gamesApp.ui.theme.HomeTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun MenuScreen(
    navigator: DestinationsNavigator
){
    val viewModel = MenuScreenViewModel()
    HomeTheme{
        MenuScreenContent(
            games = viewModel.getGames(),
            onCardClicked = { directionDestination ->  navigator.navigate(directionDestination)}
        )
    }
}

@Composable
fun MenuScreenContent(
    games: List<GameViewModel>,
    onCardClicked: (DirectionDestination) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
         Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary),
         ) {
                Text(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp),
                    text = "A list of games",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            contentPadding = PaddingValues(10.dp)
        ) {
            games.forEach{
                item{
                    GameCard(
                        game = it,
                        onCardClicked = onCardClicked
                    )
                }
                item{
                    GameCard(
                        game = it,
                        onCardClicked = onCardClicked
                    )
                }
                item{
                    GameCard(
                        game = it,
                        onCardClicked = onCardClicked
                    )
                }
            }
        }
    }
}

@Composable
fun GameCard(
    game: GameViewModel,
    onCardClicked: (DirectionDestination) -> Unit
){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .clickable { onCardClicked(game.destination) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Image(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = game.imageId),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = game.name,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Preview(
    name = "Menu Screen"
)
@Composable
fun MenuScreenPreview(){
    MenuScreenContent(
        games = listOf(
            GamePreview()
        ),
        onCardClicked = {}
    )
}

@Preview(
    name = "Game card"
)
@Composable
fun CardPreview(){
    GameCard(
        game = GamePreview(),
        onCardClicked = {}
    )
}