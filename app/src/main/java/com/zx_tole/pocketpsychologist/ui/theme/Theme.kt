package com.zx_tole.pocketpsychologist.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.zx_tole.pocketpsychologist.ui.theme.*

private val LightColorScheme = lightColorScheme(
    primary = Purple500,
    onPrimary = White,
    secondary = Teal200,
    onSecondary = Black,
    surface = Surface,
    onSurface = OnSurface,
    background = Background,
    onBackground = OnBackground
)

@Composable
fun PocketPsychologistTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
