package com.shrijal.tictactoe

// Constants to represent game state
const val IN_PROGRESS = ' '
const val DRAW = 'D'

// Check for a winner
fun checkWinner(
    board: Array<CharArray>,
    onWin: (Char) -> Unit,
    onDraw: () -> Unit
): Char {
    val winningCombinations = arrayOf(
        arrayOf(0, 0, 0, 1, 0, 2), arrayOf(1, 0, 1, 1, 1, 2), arrayOf(2, 0, 2, 1, 2, 2), // rows
        arrayOf(0, 0, 1, 0, 2, 0), arrayOf(0, 1, 1, 1, 2, 1), arrayOf(0, 2, 1, 2, 2, 2), // columns
        arrayOf(0, 0, 1, 1, 2, 2), arrayOf(0, 2, 1, 1, 2, 0)  // diagonals
    )

    // Check all winning combinations
    for (combination in winningCombinations) {
        val x1 = combination[0]
        val y1 = combination[1]
        val x2 = combination[2]
        val y2 = combination[3]
        val x3 = combination[4]
        val y3 = combination[5]

        // Check if all three positions in a combination are the same and non-empty
        if (board[x1][y1] == board[x2][y2] && board[x2][y2] == board[x3][y3] && board[x1][y1] != ' ') {
            onWin(board[x1][y1]) // Trigger the onWin callback with the winner ('X' or 'O')
            return board[x1][y1]
        }
    }

    // Check for a draw (if all cells are filled but no winner)
    if (board.all { row -> row.all { it != ' ' } }) {
        onDraw() // Trigger the onDraw callback
        return DRAW
    }

    // If no win or draw, return a placeholder character indicating the game is still in progress
    return IN_PROGRESS
}
