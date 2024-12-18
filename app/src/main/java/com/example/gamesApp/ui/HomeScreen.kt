package com.example.gamesApp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gamesApp.R
import com.example.gamesApp.ui.destinations.MenuScreenDestination
import com.example.gamesApp.ui.theme.HomeTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {
    HomeScreenContent { navigator.navigate(MenuScreenDestination()) }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
private fun HomeScreenContent(
    onContinueClicked: () -> Unit
) {
    HomeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(all = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                modifier = Modifier.padding(vertical = 64.dp),
                style = MaterialTheme.typography.titleLarge,
                text = "An assortment of games"
            )
            Spacer(modifier = Modifier.weight(2f))
            Icon(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.ic_menu_board),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.padding(horizontal = 40.dp),
                onClick = onContinueClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                interactionSource = MutableInteractionSource()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.right_arrow),
                    contentDescription = "Go to menu",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .padding(32.dp),
                text = "Made by Matt",
                style = MaterialTheme.typography.bodyMedium

            )
        }
    }
}


@Preview(
    name = "Home Screen"
)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent {}
}