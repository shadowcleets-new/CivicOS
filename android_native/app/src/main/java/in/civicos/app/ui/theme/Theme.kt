package in.civicos.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Saffron = Color(0xFFFF9933)
val White = Color.White
val Green = Color(0xFF138808)

private val ColorScheme = lightColorScheme(
    primary = Saffron,
    secondary = Green,
    background = White
)

@Composable
fun CivicOSTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        content = content
    )
}
