package com.example.composeslider.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val colorBackgroundLight = Color.White
val colorBackgroundDark = Color(red = 26, green = 28, blue = 36)


val colorSecondaryVariant = Color(red = 240, green = 243, blue = 255)
val colorSecondaryVariantDark = Color(red = 16, green = 18, blue = 23)

val Colors.colorBackground
    @Composable get() =
        if (isLight) colorBackgroundLight
        else colorBackgroundDark


val Colors.colorBackgroundVarient
    @Composable get() =
        if (isLight) colorSecondaryVariant
        else colorSecondaryVariantDark