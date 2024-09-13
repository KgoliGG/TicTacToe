package com.shrijal.tictactoe.models

import kotlin.random.Random
import kotlin.math.max
import kotlin.math.min

// Data class to represent a move
data class Move(val row: Int, val col: Int)

// Q-Table for storing Q-values
val qTable = mutableMapOf<List<CharArray>, MutableMap<Pair<Int, Int>, Double>>()

// Learning parameters
const val alpha = 0.1  // Learning rate
const val gamma = 0.9  // Discount factor
const val epsilon = 0.2  // Exploration rate

// Function to evaluate the board state
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

// Function to check if there are moves left
fun isMovesLeft(board: Array<CharArray>): Boolean {
    for (i in 0..2) {
        for (j in 0..2) {
            if (board[i][j] == ' ') return true
        }
    }
    return false
}

// Minimax function with Q-learning integration
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
                    best = max(best, minimax(board, depth + 1, false))
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
                    best = min(best, minimax(board, depth + 1, true))
                    board[i][j] = ' '
                }
            }
        }
        best
    }
}

// Function to choose the best action based on Q-learning
fun chooseAction(board: Array<CharArray>, isMaximizing: Boolean): Move {
    val availableActions = mutableListOf<Move>()
    for (i in 0..2) {
        for (j in 0..2) {
            if (board[i][j] == ' ') {
                availableActions.add(Move(i, j))
            }
        }
    }

    if (Random.nextDouble() < epsilon) {
        // Explore: Choose a random move
        return availableActions[Random.nextInt(availableActions.size)]
    } else {
        // Exploit: Choose the best move
        var bestMove = availableActions[0]
        var bestQValue = Double.MIN_VALUE
        for (action in availableActions) {
            val qValue = qTable[board.toList()]?.get(action.row to action.col) ?: 0.0
            if (qValue > bestQValue) {
                bestQValue = qValue
                bestMove = action
            }
        }
        return bestMove
    }
}

// Function to update Q-table with the result of a game
fun updateQTable(board: Array<CharArray>, action: Move, reward: Double) {
    val stateKey = board.toList()
    val oldQValue = qTable[stateKey]?.get(action.row to action.col) ?: 0.0
    val maxFutureQValue = qTable[stateKey]?.values?.maxOrNull() ?: 0.0
    val newQValue = oldQValue + alpha * (reward + gamma * maxFutureQValue - oldQValue)

    // Update Q-table
    qTable.getOrPut(stateKey) { mutableMapOf() }[action.row to action.col] = newQValue
}

// Function to find the best move considering Q-learning
fun findBestMove(board: Array<CharArray>): Move {
    return chooseAction(board, isMaximizing = true)
}
