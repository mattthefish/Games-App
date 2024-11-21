package com.example.gamesApp.engine.games

import com.example.gamesApp.R
import com.example.gamesApp.ui.destinations.TicTacToeScreenDestination


class TicTacToeViewModel : GameViewModel() {
    override val name: String = "Tic Tac Toe"
    override val imageId: Int = R.drawable.ic_menu_board
    override val destination: TicTacToeScreenDestination = TicTacToeScreenDestination

     open class Players {
        val x: X = X()
        val o: O = O()
    }

    class X : Players() {
        val name: String = "X"
        var board: Int = 0b000000000
    }

     class O : Players() {
        val name: String = "O"
        var board: Int = 0b000000000
    }

//    class PlayerTurn(var players: Players) {
//        var value: Players = players.x
//
//        fun toggle() {
//            when (this.value) {
//                is X -> this.value = this.players.o
//                is O -> this.value = this.players.x
//            }
//
//        }
//    }

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

    private fun checkWin(players: Players): String {
        return if (players.x.board in winningCombinations) players.x.name
        else if (players.o.board in winningCombinations) players.o.name
        else ""
    }

    fun getCellValue(players: Players, cellIndex: Int): String {
        return when {
            players.x.board and cellIndex == 1 -> "X"
            players.o.board and cellIndex == 1 -> "O"
            else -> ""
        }
    }

//    //index should be in bit form
//    fun tilePressed(index: Int, players: Players){
//        when (PlayerTurn(players = players).value) {
//            players.x -> {
//                players.x.board = index or players.x.board
//                PlayerTurn.toggle()
//            }
//            players.o -> {
//                players.o.board = index or players.o.board
//                PlayerTurn.toggle()
//            }
//        }
//        checkWin(players = players)
//    }
 }