package com.example.gamesApp.engine.games

import androidx.compose.ui.graphics.Color
import com.example.gamesApp.R
import com.example.gamesApp.ui.destinations.TicTacToeScreenDestination
import com.example.gamesApp.ui.theme.PlayerBlue
import com.example.gamesApp.ui.theme.PlayerOrange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TicTacToeViewModel : GameViewModel() {
    override val name: String = "Tic Tac Toe"
    override val imageId: Int = R.drawable.ic_menu_board
    override val destination: TicTacToeScreenDestination = TicTacToeScreenDestination

    private val internalState = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = internalState

     data class Player (
         val name: String,
         val color: Color,
     )

    enum class Turn {
        X,
        O
    }

    private fun switchTurn() {
        internalState.value = state.value.copy(
            playerTurn = when(state.value.playerTurn) {
                Turn.O ->  Turn.X
                Turn.X ->  Turn.O
            }
        )
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

    private fun checkWin(): String? {
        for(combination in winningCombinations) {
            val (a, b, c) = combination
            val board = state.value.board
            board[a].let {
                if (board[a]?.name == board[b]?.name && board[b]?.name == board[c]?.name) {
                    return board[a]?.name
                }
            }
        }
        return null
    }

    fun resetBoard(){
        internalState.value = GameState()
    }

    fun tilePressed(index: Int){
        if (!state.value.board.contains(null)) internalState.value = state.value.copy(isGameOver = true)

        if (state.value.board[index] == null) {
            internalState.value = state.value.copy(
                board = state.value.board.toMutableList().apply {
                    this[index] = if (state.value.playerTurn == Turn.X) state.value.x else state.value.o
                }
            )
            switchTurn()
            val winner = checkWin()
            when (winner) {
                state.value.x.name  ->
                    { internalState.value = state.value.copy(isGameOver = true, winner = state.value.x) }
                state.value.o.name  ->
                    { internalState.value = state.value.copy(isGameOver = true, winner = state.value.o) }
                null -> if (state.value.board.contains(null))
                    {/* do nothing */}
                    else { internalState.value = state.value.copy(isGameOver = true)}
            }
        }
    }

    data class GameState(
        val playerTurn: Turn = Turn.O,
        val board: List<Player?> = MutableList(9) {null},
        val isGameOver: Boolean = false,
        val winner: Player? = null
    ) {
        val x = Player(
            name = "X",
            color = PlayerBlue,
        )
        val o = Player(
            name = "O",
            color = PlayerOrange,
        )
    }
}