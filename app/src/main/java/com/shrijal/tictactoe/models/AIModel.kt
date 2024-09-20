package com.shrijal.tictactoe.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.random.Random

// Data class to represent a move
data class Move(val row: Int, val col: Int)

// Typealias for Q-table
typealias QTable = MutableMap<String, MutableMap<String, Float>>

// Learning parameters
const val alpha = 1f  // Learning rate
const val gamma = 1f  // Discount factor
const val epsilon = 10  // Exploration rate

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

// Function to convert board to a string for Q-table key
fun boardToString(board: Array<CharArray>): String {
    return board.joinToString(separator = "") { it.joinToString(separator = "") }
}

// Function to initialize Q-values for available moves
fun initializeMoves(board: Array<CharArray>): MutableMap<String, Float> {
    val moves = mutableMapOf<String, Float>()
    for (i in 0..2) {
        for (j in 0..2) {
            if (board[i][j] == ' ') {
                moves["$i,$j"] = 0f // Initialize Q-values for each move
            }
        }
    }
    return moves
}

// Function to choose action based on Q-learning (epsilon-greedy)
fun chooseAction(board: Array<CharArray>, qTable: QTable): Move {
    val availableActions = mutableListOf<Move>()
    for (i in 0..2) {
        for (j in 0..2) {
            if (board[i][j] == ' ') {
                availableActions.add(Move(i, j))
            }
        }
    }

    return if (Random.nextDouble() < epsilon) {
        // Explore: Choose a random move
        availableActions[Random.nextInt(availableActions.size)]
    } else {
        // Exploit: Choose the best move based on Q-table
        var bestMove = availableActions[0]
        var bestQValue = Float.MIN_VALUE
        for (action in availableActions) {
            val actionKey = "${action.row},${action.col}"
            val qValue = qTable[boardToString(board)]?.get(actionKey) ?: 0f
            if (qValue > bestQValue) {
                bestQValue = qValue
                bestMove = action
            }
        }
        bestMove
    }
}

// Function to find the best move using Q-table
fun findBestMove(board: Array<CharArray>, qTable: QTable): Move {
    val state = boardToString(board)
    val moves = qTable[state] ?: initializeMoves(board)

    // Pick the move with the highest Q-value
    val bestAction = moves.maxByOrNull { it.value }?.key ?: "${0},${0}"
    val parts = bestAction.split(",")
    return Move(parts[0].toInt(), parts[1].toInt())
}

// Function to update Q-values based on game result
fun updateQValues(qTable: QTable, board: Array<CharArray>, result: String) {
    val state = boardToString(board)
    val moves = qTable[state] ?: return

    val reward = when (result) {
        "win" -> 1f  // AI wins
        "lose" -> -1f  // AI loses
        "draw" -> 0f  // Draw
        else -> 0f
    }

    // Update Q-values using the Q-learning formula
    moves.forEach { (action, qValue) ->
        val newQValue = qValue + alpha * (reward - qValue)
        moves[action] = newQValue
    }
}

// Function to save Q-table to SharedPreferences
fun saveQTable(context: Context, qTable: QTable) {
    val sharedPref = context.getSharedPreferences("QTable", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    val json = Gson().toJson(qTable)
    editor.putString("qTable", json)
    editor.apply()
}

// Function to load Q-table from SharedPreferences
fun loadQTable(context: Context): QTable {
    val sharedPref = context.getSharedPreferences("QTable", Context.MODE_PRIVATE)
    val json = sharedPref.getString("QTable", null)
    return if (json != null) {
        val type = object : TypeToken<MutableMap<String, MutableMap<String, Float>>>() {}.type
        Gson().fromJson(json, type)
    } else {
        mutableMapOf()
    }
}
