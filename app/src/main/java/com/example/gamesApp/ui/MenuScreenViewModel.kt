package com.example.gamesApp.ui

import com.example.gamesApp.engine.games.Game
import com.example.gamesApp.engine.games.TicTacToe

class MenuScreenViewModel{
private val gamesList: List<Game> = listOf(
    TicTacToe(),
    //Add more games here
    )

    fun getGames() = this.gamesList
}
