package com.shrijal.tictactoe.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrijal.tictactoe.ui.theme.montserrat

@Composable
fun GameTitle(){
    Text(
        text = "Tic-Tac-Toe",
        style = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight(800),
            fontSize = 50.sp,
            color = Color.White,
        )
    )
}

@Composable
fun GameModeTitle(
    text: String
){
    Text(
        text = text.uppercase(),
        modifier = Modifier
            .fillMaxWidth(),
        style = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight(400),
            fontSize = 16.sp,
            color = Color.White,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center,
            )
    )
}

@Composable
fun ErrorMessage(value: String){
    Text(
        text = value,
        style = TextStyle(
            color = Color.Red,
            fontFamily = montserrat,
            fontSize = 12.sp,
            fontWeight = FontWeight(500)
        )
    )
}

@Composable
fun CheckboxComponentes (
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onSelectedText: (String) -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
//        Log.d("Statechange", "{${checked}}")
//        Log.d("Statechanged", "{${onCheckedChange}}")
        ClickableTextComponent(
            onSelectedText = onSelectedText,
        )
    }
}

@Composable
fun ClickableTextComponent(onSelectedText : (String) -> Unit){
    val initialText = "I accept all the "
    val termsText = "Terms of Use"
    val and = " and "
    val policyText = "Privacy Policies"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(
            style = SpanStyle(
                color = Color.White,
                textDecoration = TextDecoration.Underline
            )
        ){
            pushStringAnnotation(tag = termsText, annotation = termsText)
            append(termsText)
        }
        append(and)
        withStyle(
            style = SpanStyle(
                color = Color.White,
                textDecoration = TextDecoration.Underline
            )
        ){
            pushStringAnnotation(tag = policyText, annotation = policyText)
            append(policyText)
        }
    }

    ClickableText(
        text = annotatedString,
        onClick = {
                offset ->
            annotatedString.getStringAnnotations(offset,offset)
                .firstOrNull()?.also {
                        span ->
//                    Log.d("ClickableTextComponent","{$span}")
                    if (span.item == termsText){
                        onSelectedText(span.item)
                    }
                }
        },
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = montserrat,
            fontWeight = FontWeight(600),
            color = Color(0xFFFFFFFF),
            letterSpacing = 0.2.sp
        )
    )
}