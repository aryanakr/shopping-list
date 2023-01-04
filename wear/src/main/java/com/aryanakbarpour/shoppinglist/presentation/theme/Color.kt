package com.aryanakbarpour.shoppinglist.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val Red400 = Color(0xFFCF6679)

val Accent = Color(0xFF477A5D)
val PrimaryLight = Color(0xFFEDF59A)
val PrimaryDark = Color(0xFF7A8425)
val Primary = Color(0xFFD5E341)
val Background = Color(0xFF343A07)

internal val wearColorPalette: Colors = Colors(
    primary = Primary,
    primaryVariant = PrimaryDark,
    secondary = Accent,
    secondaryVariant = PrimaryLight,
    error = Red400,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onError = Color.Black,
    background = Background,
)