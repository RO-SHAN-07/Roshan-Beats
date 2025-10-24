package org.roshan.beats.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.roshan.beats.data.models.Track
import org.roshan.beats.ui.theme.*
import org.roshan.beats.viewmodel.PlayerViewModel

@Composable
fun CarModeScreen(
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier
) {
    val playerState by playerViewModel.playerState.collectAsState()
    val track = playerState.currentTrack
    
    // Large, touch-friendly interface for driving
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (track != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Track info - large and readable
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = track.name,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = track.artistName,
                        fontSize = 24.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                
                // Large playback controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous
                    CarModeButton(
                        icon = Icons.Default.SkipPrevious,
                        onClick = { playerViewModel.playPrevious() },
                        size = 100.dp
                    )
                    
                    // Play/Pause - extra large
                    CarModeButton(
                        icon = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        onClick = { playerViewModel.togglePlayPause() },
                        size = 140.dp,
                        isPrimary = true
                    )
                    
                    // Next
                    CarModeButton(
                        icon = Icons.Default.SkipNext,
                        onClick = { playerViewModel.playNext() },
                        size = 100.dp
                    )
                }
                
                // Additional controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CarModeButton(
                        icon = Icons.Default.Shuffle,
                        onClick = { playerViewModel.toggleShuffle() },
                        size = 80.dp,
                        isActive = playerState.isShuffleEnabled
                    )
                    
                    CarModeButton(
                        icon = Icons.Default.Repeat,
                        onClick = { playerViewModel.cycleRepeatMode() },
                        size = 80.dp,
                        isActive = playerState.repeatMode != org.roshan.beats.viewmodel.RepeatMode.OFF
                    )
                    
                    CarModeButton(
                        icon = Icons.Default.VolumeUp,
                        onClick = { /* Volume control */ },
                        size = 80.dp
                    )
                }
            }
        } else {
            // No track playing
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No track playing",
                    fontSize = 32.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun CarModeButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    size: Dp,
    isPrimary: Boolean = false,
    isActive: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = Modifier
            .size(size)
            .scale(if (isPrimary) pulse else 1f)
            .clip(CircleShape)
            .background(
                brush = when {
                    isPrimary -> Brush.radialGradient(
                        colors = listOf(ElectricBlue, ElectricViolet)
                    )
                    isActive -> Brush.radialGradient(
                        colors = listOf(ElectricBlue.copy(alpha = 0.5f), ElectricBlue.copy(alpha = 0.3f))
                    )
                    else -> Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.2f), Color.White.copy(alpha = 0.1f))
                    )
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(size * 0.5f),
            tint = Color.White
        )
    }
}
