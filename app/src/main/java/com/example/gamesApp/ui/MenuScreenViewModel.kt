package com.example.gamesApp.ui

import com.example.gamesApp.engine.games.GameViewModel
import com.example.gamesApp.engine.games.TicTacToeViewModel

class MenuScreenViewModel{
private val gamesList: List<GameViewModel> = listOf(
    TicTacToeViewModel(),
    //Add more games here
    )

    fun getGames() = this.gamesList
}
