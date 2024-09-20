package com.shrijal.tictactoe.composable

import kotlin.random.Random

//Reset the board
fun reset(
    board: Array<CharArray>,
    player1: ArrayList<Int>,
    player2: ArrayList<Int>,
    emptyCells: ArrayList<Int>,
    onResetActiveUser: (newActiveUser: Int) -> Unit
) {
    // Clear the board by setting all cells to ' ' (empty space)
    for (i in board.indices) {
        for (j in board[i].indices) {
            board[i][j] = ' ' // Reset to space, not empty string
        }
    }

    // Clear player moves
    player1.clear()
    player2.clear()

    // Reinitialize the list of empty cells
    emptyCells.clear()
    for (i in 0 until 9) {
        emptyCells.add(i)
    }

    // Randomly set the first player (1 for X, 2 for O)
    val newActiveUser = Random.nextInt(1, 3)
    onResetActiveUser(newActiveUser)
}
