package com.shrijal.tictactoe.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrijal.tictactoe.ui.theme.Secondary
import com.shrijal.tictactoe.ui.theme.Tertiary
import com.shrijal.tictactoe.ui.theme.montserrat

@Composable
fun Scoreboard(wincountplayer1: Int, wincountplayer2: Int, drawCount: Int, modifier: Modifier = Modifier){
    Column(
        modifier = Modifier
            .fillMaxWidth(.9f),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Draws: $drawCount", // Display draw count
            style = TextStyle(
                fontFamily = montserrat,
                fontWeight = FontWeight(400),
                fontSize = 18.sp,
                color = Color.White,
            )
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = montserrat,
                            fontWeight = FontWeight(600),
                            fontSize = 32.sp,
                            color = Secondary,
                        )
                    ){
                        append("X")
                    }
                    withStyle(
                            style = SpanStyle(
                                fontFamily = montserrat,
                                fontWeight = FontWeight(400),
                                fontSize = 18.sp,
                                color = Color.White,
                            )
                            ){
                        append(" : ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = montserrat,
                            fontWeight = FontWeight(600),
                            fontSize = 32.sp,
                            color = Color.White,
                        )
                    ){
                        append("$wincountplayer1")
                    }
                }
//                text = "Player X: $wincountplayer1",
//                style = TextStyle(
//                    fontFamily = montserrat,
//                    fontWeight = FontWeight(400),
//                    fontSize = 18.sp,
//                    color = Color.White,
//                )
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = montserrat,
                            fontWeight = FontWeight(600),
                            fontSize = 32.sp,
                            color = Tertiary,
                        )
                    ){
                        append("O")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = montserrat,
                            fontWeight = FontWeight(400),
                            fontSize = 18.sp,
                            color = Color.White,
                        )
                    ){
                        append(" : ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = montserrat,
                            fontWeight = FontWeight(600),
                            fontSize = 32.sp,
                            color = Color.White,
                        )
                    ){
                        append("$wincountplayer2")
                    }
                }
            )
        }
    }
}