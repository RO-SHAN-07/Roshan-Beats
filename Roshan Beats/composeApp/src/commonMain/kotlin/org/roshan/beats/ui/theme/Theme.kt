package org.roshan.beats.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = VividBlue,
    onPrimaryContainer = Color.White,
    
    secondary = ElectricViolet,
    onSecondary = Color.White,
    secondaryContainer = DeepViolet,
    onSecondaryContainer = Color.White,
    
    tertiary = NeonPurple,
    onTertiary = Color.White,
    
    background = DeepCharcoal,
    onBackground = TextPrimary,
    
    surface = MatteBlack,
    onSurface = TextPrimary,
    surfaceVariant = DarkGray,
    onSurfaceVariant = TextSecondary,
    
    surfaceTint = ElectricBlue,
    
    error = Color(0xFFFF5252),
    onError = Color.White,
    
    outline = TextSecondary,
    outlineVariant = TextTertiary
)

private val LightColorScheme = lightColorScheme(
    primary = VividBlue,
    onPrimary = Color.White,
    primaryContainer = ElectricBlue.copy(alpha = 0.2f),
    onPrimaryContainer = VividBlue,
    
    secondary = DeepViolet,
    onSecondary = Color.White,
    secondaryContainer = ElectricViolet.copy(alpha = 0.2f),
    onSecondaryContainer = DeepViolet,
    
    tertiary = NeonPurple,
    onTertiary = Color.White,
    
    background = LightBackground,
    onBackground = Color.Black,
    
    surface = LightSurface,
    onSurface = Color.Black,
    surfaceVariant = LightGray,
    onSurfaceVariant = Color.Black.copy(alpha = 0.6f),
    
    surfaceTint = VividBlue,
    
    error = Color(0xFFD32F2F),
    onError = Color.White,
    
    outline = Color.Black.copy(alpha = 0.5f),
    outlineVariant = Color.Black.copy(alpha = 0.3f)
)

@Composable
fun RoshanBeatsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
