package com.shrijal.tictactoe.composable

fun codeCheck(code: String): Boolean {
    return code.length == 6 && code.all {
        it.isDigit()
    }
}