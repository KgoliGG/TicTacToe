package com.shrijal.tictactoe.models

data class Move(val row: Int, val col: Int)

fun minimax(board: Array<CharArray>, depth: Int, isMaximizing: Boolean): Int {
    val score = evaluate(board)
    if (score == 10) return score - depth // Prioritize faster wins
    if (score == -10) return score + depth // Prioritize slower losses
    if (!isMovesLeft(board)) return 0

    return if (isMaximizing) {
        var best = Int.MIN_VALUE
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'X'
                    best = maxOf(best, minimax(board, depth + 1, false))
                    board[i][j] = ' '
                }
            }
        }
        best
    } else {
        var best = Int.MAX_VALUE
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'O'
                    best = minOf(best, minimax(board, depth + 1, true))
                    board[i][j] = ' '
                }
            }
        }
        best
    }
}

fun findBestMove(board: Array<CharArray>): Move {
    var bestVal = Int.MIN_VALUE
    var bestMove = Move(-1, -1)

    for (i in 0..2) {
        for (j in 0..2) {
            if (board[i][j] == ' ') {
                board[i][j] = 'X'
                val moveVal = minimax(board, 0, false)
                board[i][j] = ' '

                if (moveVal > bestVal) {
                    bestMove = Move(i, j)
                    bestVal = moveVal
                }
            }
        }
    }
    return bestMove
}

fun evaluate(board: Array<CharArray>): Int {
    // Checking Rows for X or O victory.
    for (row in 0..2) {
        if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
            if (board[row][0] == 'X') return 10
            if (board[row][0] == 'O') return -10
        }
    }

    // Checking Columns for X or O victory.
    for (col in 0..2) {
        if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
            if (board[0][col] == 'X') return 10
            if (board[0][col] == 'O') return -10
        }
    }

    // Checking Diagonals for X or O victory.
    if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
        if (board[0][0] == 'X') return 10
        if (board[0][0] == 'O') return -10
    }
    if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
        if (board[0][2] == 'X') return 10
        if (board[0][2] == 'O') return -10
    }

    // No winner yet
    return 0
}

fun isMovesLeft(board: Array<CharArray>): Boolean {
    for (i in 0..2) {
        for (j in 0..2) {
            if (board[i][j] == ' ') return true
        }
    }
    return false
}
