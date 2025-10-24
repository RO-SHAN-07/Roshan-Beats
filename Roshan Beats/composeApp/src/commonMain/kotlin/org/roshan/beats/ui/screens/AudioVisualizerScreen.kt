package org.roshan.beats.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.roshan.beats.data.models.Track
import org.roshan.beats.ui.theme.*
import kotlin.math.*

enum class VisualizerMode {
    BARS, WAVE, CIRCULAR, PARTICLE, SPECTRUM
}

@Composable
fun AudioVisualizerScreen(
    track: Track?,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    var selectedMode by remember { mutableStateOf(VisualizerMode.BARS) }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when (selectedMode) {
            VisualizerMode.BARS -> BarsVisualizer(isPlaying)
            VisualizerMode.WAVE -> WaveVisualizer(isPlaying)
            VisualizerMode.CIRCULAR -> CircularVisualizer(isPlaying)
            VisualizerMode.PARTICLE -> ParticleVisualizer(isPlaying)
            VisualizerMode.SPECTRUM -> SpectrumVisualizer(isPlaying)
        }
        
        // Track info overlay
        if (track != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = track.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = track.artistName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
        
        // Mode selector at bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
                .background(
                    Color.Black.copy(alpha = 0.5f),
                    RoundedCornerShape(16.dp)
                )
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            VisualizerMode.entries.forEach { mode ->
                VisualizerModeButton(
                    mode = mode,
                    isSelected = selectedMode == mode,
                    onClick = { selectedMode = mode }
                )
            }
        }
    }
}

@Composable
private fun BarsVisualizer(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition()
    val bars = remember { (0..31).map { it } }
    
    val animatedValues = bars.map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 300 + index * 20,
                    easing = EaseInOut
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
    }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val barWidth = size.width / bars.size
        val centerY = size.height / 2
        
        bars.forEachIndexed { index, _ ->
            val value = if (isPlaying) animatedValues[index].value else 0.2f
            val barHeight = value * size.height * 0.4f
            val x = index * barWidth + barWidth / 2
            
            drawLine(
                brush = Brush.verticalGradient(
                    colors = listOf(ElectricBlue, ElectricViolet, NeonPurple)
                ),
                start = Offset(x, centerY - barHeight),
                end = Offset(x, centerY + barHeight),
                strokeWidth = barWidth * 0.7f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun WaveVisualizer(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition()
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing)
        )
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val path = Path()
        val amplitude = size.height * 0.2f
        val frequency = 4f
        
        path.moveTo(0f, size.height / 2)
        
        for (x in 0..size.width.toInt() step 5) {
            val y = if (isPlaying) {
                size.height / 2 + amplitude * sin(x / size.width * 2 * PI * frequency + phase).toFloat()
            } else {
                size.height / 2
            }
            path.lineTo(x.toFloat(), y)
        }
        
        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(ElectricBlue, ElectricViolet, NeonPurple, ElectricBlue)
            ),
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun CircularVisualizer(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing)
        )
    )
    
    val circles = remember { (0..5).map { it } }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val maxRadius = minOf(size.width, size.height) * 0.4f
        
        circles.forEach { index ->
            val radius = if (isPlaying) {
                maxRadius * (index + 1) / circles.size
            } else {
                maxRadius * 0.3f
            }
            
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ElectricBlue.copy(alpha = 0.3f),
                        ElectricViolet.copy(alpha = 0.1f),
                        Color.Transparent
                    )
                ),
                radius = radius,
                center = Offset(centerX, centerY)
            )
        }
    }
}

@Composable
private fun ParticleVisualizer(isPlaying: Boolean) {
    val particles = remember { (0..50).map { it } }
    val infiniteTransition = rememberInfiniteTransition()
    
    val particlePositions = particles.map { index ->
        val offsetX by infiniteTransition.animateFloat(
            initialValue = -1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000 + index * 50, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        val offsetY by infiniteTransition.animateFloat(
            initialValue = -1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2500 + index * 50, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        offsetX to offsetY
    }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val maxDistance = minOf(size.width, size.height) * 0.3f
        
        particles.forEachIndexed { index, _ ->
            if (isPlaying) {
                val (offsetX, offsetY) = particlePositions[index]
                val x = centerX + offsetX * maxDistance
                val y = centerY + offsetY * maxDistance
                
                drawCircle(
                    color = when (index % 3) {
                        0 -> ElectricBlue
                        1 -> ElectricViolet
                        else -> NeonPurple
                    },
                    radius = 6.dp.toPx(),
                    center = Offset(x, y),
                    alpha = 0.7f
                )
            }
        }
    }
}

@Composable
private fun SpectrumVisualizer(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition()
    val frequencies = remember { (0..64).map { it } }
    
    val animatedValues = frequencies.map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 200 + index * 10,
                    easing = EaseInOut
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
    }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val barWidth = size.width / frequencies.size
        
        frequencies.forEachIndexed { index, _ ->
            val value = if (isPlaying) animatedValues[index].value else 0.1f
            val barHeight = value * size.height * 0.8f
            val x = index * barWidth + barWidth / 2
            val y = size.height - barHeight
            
            val color = when {
                index < frequencies.size / 3 -> ElectricBlue
                index < 2 * frequencies.size / 3 -> ElectricViolet
                else -> NeonPurple
            }
            
            drawLine(
                color = color,
                start = Offset(x, size.height),
                end = Offset(x, y),
                strokeWidth = barWidth * 0.8f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun VisualizerModeButton(
    mode: VisualizerMode,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val icon = when (mode) {
        VisualizerMode.BARS -> Icons.Default.BarChart
        VisualizerMode.WAVE -> Icons.Default.ShowChart
        VisualizerMode.CIRCULAR -> Icons.Default.Circle
        VisualizerMode.PARTICLE -> Icons.Default.GrainOutlined
        VisualizerMode.SPECTRUM -> Icons.Default.GraphicEq
    }
    
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                if (isSelected) ElectricBlue.copy(alpha = 0.3f) else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = mode.name,
            tint = if (isSelected) ElectricBlue else Color.White.copy(alpha = 0.5f),
            modifier = Modifier.size(32.dp)
        )
    }
}
