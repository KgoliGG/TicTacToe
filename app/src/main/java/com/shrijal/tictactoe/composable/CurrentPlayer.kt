package com.shrijal.tictactoe.composable

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrijal.tictactoe.ui.theme.montserrat

@Composable
fun CurrentPlayerText(currentPlayer: String, modifier: Modifier = Modifier) {
    if (currentPlayer != "") {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = montserrat,
                        fontWeight = FontWeight(400),
                        fontSize = 18.sp,
                        color = Color.White,
                    )
                ) {
                    append("Current Player : ")
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = montserrat,
                        fontWeight = FontWeight(600),
                        fontSize = 28.sp,
                        color = Color.White,
                    )
                ) {
                    append(currentPlayer)
                }
            },
            modifier = modifier.height(30.dp)
        )
    }
    else {
        Text(
            text = "",
            fontSize = 24.sp,
            modifier = Modifier.height(30.dp)
        )
    }
}