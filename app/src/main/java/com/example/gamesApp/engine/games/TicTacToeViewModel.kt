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

    val gameState = GameState()

     data class Player (
         val name: String,
         val color: Color,
     )

    enum class Turn {
        X,
        O
    }

    class GameState {
        var playerTurn: Turn = Turn.O
        var board: MutableList<Player?> = mutableListOf(null, null, null, null, null, null, null, null, null)

        val x = Player(
            name = "X",
            color = PlayerBlue,
        )
        val o = Player(
            name = "O",
            color = PlayerOrange,
        )
    }

    private fun switchTurn() {
        gameState.playerTurn = when(gameState.playerTurn) {
            Turn.O ->  Turn.X
            Turn.X ->  Turn.O
        }
    }

    private val winningCombinations = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )

    private fun checkWin(): Player? {
        for(combination in winningCombinations) {
            val (a, b, c) = combination
            val board = gameState.board
            board[a].let {
                if (board[a] == board[b] && board[b] == board[c]) {
                    return board[a]
                }
            }
        }
        return null
    }

    fun getCellValue(cellIndex: Int): String {
        return gameState.board[cellIndex]?.name ?: ""
    }

    fun tilePressed(index: Int){
        when (gameState.playerTurn) {
            Turn.X -> {
                gameState.board[index] = gameState.x
                switchTurn()
            }
            Turn.O -> {
                gameState.board[index] = gameState.o
                switchTurn()
            }
        }
        checkWin()
    }
}