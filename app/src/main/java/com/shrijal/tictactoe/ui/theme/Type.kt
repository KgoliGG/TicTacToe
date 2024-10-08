package com.shrijal.tictactoe.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.shrijal.tictactoe.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
val montserrat = FontFamily(
    Font(R.font.montserrat_normal, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_extrabold, FontWeight.ExtraBold),
    Font(R.font.montserrat_black, FontWeight.Black),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_light, FontWeight.Light),
    Font(R.font.montserrat_extralight, FontWeight.ExtraLight),
    Font(R.font.montserrat_thin, FontWeight.Thin),
)

val italicmontserrat = FontFamily(
    Font(R.font.montserrat_italic, FontWeight.Normal),
    Font(R.font.montserrat_bolditalic, FontWeight.Bold),
    Font(R.font.montserrat_extrabolditalic, FontWeight.ExtraBold),
    Font(R.font.montserrat_blackitalic, FontWeight.Black),
    Font(R.font.montserrat_mediumitalic, FontWeight.Medium),
    Font(R.font.montserrat_semibolditalic, FontWeight.SemiBold),
    Font(R.font.montserrat_lightitalic, FontWeight.Light),
    Font(R.font.montserrat_extralightitalic, FontWeight.ExtraLight),
    Font(R.font.montserrat_thinitalic, FontWeight.Thin),
)
