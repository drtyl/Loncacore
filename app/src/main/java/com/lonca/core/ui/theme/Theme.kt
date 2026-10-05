package com.lonca.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LoncaRenkSemasi = darkColorScheme(
    background = LoncaArkaplan,
    surface = LoncaKart,
    primary = LoncaVurgu,
    onPrimary = Color.Black,
    onBackground = LoncaMetin,
    onSurface = LoncaMetin,
    outline = LoncaCizgi
)

@Composable
fun LoncaCoreTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LoncaRenkSemasi,
        typography = LoncaYazi,
        content = content
    )
}
