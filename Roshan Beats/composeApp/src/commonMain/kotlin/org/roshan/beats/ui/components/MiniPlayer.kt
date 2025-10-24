package org.roshan.beats.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.roshan.beats.data.models.Track
import org.roshan.beats.viewmodel.PlayerViewModel

@Composable
fun MiniPlayer(
    playerViewModel: PlayerViewModel,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playerState by playerViewModel.playerState.collectAsState()
    val track = playerState.currentTrack
    
    if (track != null) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(72.dp)
                .clickable(onClick = onExpand),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Album art with playing animation
                Box {
                    AsyncImage(
                        model = track.coverArt,
                        contentDescription = track.name,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface),
                        contentScale = ContentScale.Crop
                    )
                    
                    if (playerState.isPlaying) {
                        PlayingAnimation(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Track info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = track.name,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = track.artistName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Play/Pause button
                IconButton(
                    onClick = { playerViewModel.togglePlayPause() }
                ) {
                    Icon(
                        imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (playerState.isPlaying) "Pause" else "Play",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                // Next button
                IconButton(
                    onClick = { playerViewModel.playNext() }
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Progress bar
            LinearProgressIndicator(
                progress = {
                    val duration = playerState.duration.takeIf { it > 0 } ?: 1L
                    (playerState.currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun PlayingAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    
    val bar1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(550, delayMillis = 200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Row(
        modifier = modifier
            .size(20.dp)
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight(bar1)
                .background(
                    MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(1.dp)
                )
        )
        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight(bar2)
                .background(
                    MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(1.dp)
                )
        )
        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight(bar3)
                .background(
                    MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(1.dp)
                )
        )
    }
}
