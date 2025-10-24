package org.roshan.beats.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.roshan.beats.ui.theme.DeepCharcoal
import org.roshan.beats.ui.theme.ElectricBlue
import org.roshan.beats.ui.theme.ElectricViolet
import org.roshan.beats.ui.theme.JetBlack

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    
    // Logo scale and alpha animation
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(800, easing = FastOutSlowInEasing)
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800)
    )
    
    // Pulsing glow animation
    val infiniteTransition = rememberInfiniteTransition()
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    // Soundwave animation
    val wave1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val wave2 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, delayMillis = 200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val wave3 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, delayMillis = 400, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        onSplashComplete()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepCharcoal,
                        JetBlack,
                        ElectricViolet.copy(alpha = 0.1f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo text with glow effect
            Text(
                text = "Roshan Beats",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricBlue.copy(alpha = glowPulse),
                modifier = Modifier
                    .scale(scale)
                    .alpha(alpha)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Animated soundwave bars
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(alpha)
            ) {
                SoundWaveBar(height = 40.dp * wave1)
                SoundWaveBar(height = 60.dp * wave2)
                SoundWaveBar(height = 50.dp * wave3)
                SoundWaveBar(height = 70.dp * wave1)
                SoundWaveBar(height = 45.dp * wave2)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Tagline
            Text(
                text = "Feel the Future of Sound",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f * alpha),
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
private fun SoundWaveBar(height: Dp) {
    Box(
        modifier = Modifier
            .width(6.dp)
            .height(height)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ElectricBlue,
                        ElectricViolet
                    )
                ),
                shape = MaterialTheme.shapes.small
            )
    )
}
