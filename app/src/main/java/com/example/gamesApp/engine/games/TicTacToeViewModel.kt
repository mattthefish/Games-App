package com.example.gamesApp.engine.games

import androidx.compose.ui.graphics.Color
import com.example.gamesApp.R
import com.example.gamesApp.ui.destinations.TicTacToeScreenDestination
import com.example.gamesApp.ui.theme.PlayerBlue
import com.example.gamesApp.ui.theme.PlayerOrange

class TicTacToeViewModel : GameViewModel() {
    override val name: String = "Tic Tac Toe"
    override val imageId: Int = R.drawable.ic_menu_board
    override val destination: TicTacToeScreenDestination = TicTacToeScreenDestination

    private val gameState = GameState()

     data class Player (
         val name: String,
         val color: Color,
         var board: Int
     )

    enum class Turn {
        X,
        O
    }

    class GameState {
        val x = Player(
            name = "X",
            color = PlayerBlue,
            board = 0b000000000
        )
        val o = Player(
            name = "O",
            color = PlayerOrange,
            board = 0b000000000
        )
        var playerTurn: Turn = Turn.O
    }

    private fun switchTurn() {
        gameState.playerTurn = when(gameState.playerTurn) {
            Turn.O ->  Turn.X
            Turn.X ->  Turn.O
        }
    }

    private val winningCombinations = listOf(
        0b111000000,
        0b000111000,
        0b000000111,
        0b100100100,
        0b010010010,
        0b001001001,
        0b100010001,
        0b001010100
    )

    private fun checkWin(): String {
        return if (gameState.x.board in winningCombinations) gameState.x.name
        else if (gameState.o.board in winningCombinations) gameState.o.name
        else ""
    }

    fun getCellValue(cellIndex: Int): String {
        return when {
            gameState.x.board and cellIndex == 1 -> "X"
            gameState.o.board and cellIndex == 1 -> "O"
            else -> ""
        }
    }

    //index should be in bit form
    fun tilePressed(index: Int){
        when (gameState.playerTurn) {
            Turn.X -> {
                gameState.x.board = index or gameState.x.board
                switchTurn()
            }
            Turn.O -> {
                gameState.o.board = index or gameState.o.board
                switchTurn()
            }
        }
        checkWin()
    }
}