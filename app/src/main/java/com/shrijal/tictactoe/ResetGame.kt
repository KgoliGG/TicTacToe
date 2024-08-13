package com.shrijal.tictactoe

import kotlin.random.Random

//Reset the board
fun reset(
    board: Array<Array<String>>,
    player1: ArrayList<Int>,
    player2: ArrayList<Int>,
    emptyCells: ArrayList<Int>,
    activeUserSetter: (Int) -> Unit // Pass a function to set activeUser
) {
    // Reset the board, but don't clear the win counts
    for (i in 0..2) {
        for (j in 0..2) {
            board[i][j] = ""
        }
    }
    player1.clear()
    player2.clear()
    emptyCells.clear()
    // Randomly select the first player for the new game
    activeUserSetter(Random.nextInt(1, 3))
}
