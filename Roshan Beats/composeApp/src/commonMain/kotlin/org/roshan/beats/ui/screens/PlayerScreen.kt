package org.roshan.beats.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.roshan.beats.data.models.Track
import org.roshan.beats.ui.theme.*
import org.roshan.beats.viewmodel.PlayerViewModel
import org.roshan.beats.viewmodel.RepeatMode
import kotlin.math.roundToInt

@Composable
fun PlayerScreen(
    playerViewModel: PlayerViewModel,
    onShowQueue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playerState by playerViewModel.playerState.collectAsState()
    val track = playerState.currentTrack
    
    if (track == null) {
        EmptyPlayerState(modifier)
    } else {
        PlayerContent(
            track = track,
            playerViewModel = playerViewModel,
            onShowQueue = onShowQueue,
            modifier = modifier
        )
    }
}

@Composable
private fun EmptyPlayerState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepCharcoal, JetBlack)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No track playing",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PlayerContent(
    track: Track,
    playerViewModel: PlayerViewModel,
    onShowQueue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playerState by playerViewModel.playerState.collectAsState()
    
    // Pulsing animation for album art
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Blurred background from album art
        AsyncImage(
            model = track.coverArt,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(50.dp),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )
        
        // Dark gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.9f),
                            Color.Black
                        )
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Album art
            AsyncImage(
                model = track.coverArt,
                contentDescription = track.name,
                modifier = Modifier
                    .size(320.dp)
                    .scale(if (playerState.isPlaying) scale else 1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Track info
            Text(
                text = track.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = track.artistName,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            
            if (track.albumName.isNotEmpty()) {
                Text(
                    text = track.albumName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Progress bar
            ProgressBar(
                currentPosition = playerState.currentPosition,
                duration = playerState.duration.takeIf { it > 0 } ?: (track.duration * 1000L),
                onSeek = { playerViewModel.seekTo(it) }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Playback controls
            PlaybackControls(
                isPlaying = playerState.isPlaying,
                isShuffleEnabled = playerState.isShuffleEnabled,
                repeatMode = playerState.repeatMode,
                onPlayPauseClick = { playerViewModel.togglePlayPause() },
                onPreviousClick = { playerViewModel.playPrevious() },
                onNextClick = { playerViewModel.playNext() },
                onShuffleClick = { playerViewModel.toggleShuffle() },
                onRepeatClick = { playerViewModel.cycleRepeatMode() }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Bottom actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: Like */ }) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                IconButton(onClick = { /* TODO: Download */ }) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                IconButton(onClick = onShowQueue) {
                    Icon(
                        imageVector = Icons.Default.QueueMusic,
                        contentDescription = "Queue",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                IconButton(onClick = { /* TODO: Share */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                IconButton(onClick = { playerViewModel.toggleAiMode() }) {
                    Icon(
                        imageVector = if (playerState.isAiModeEnabled) Icons.Default.AutoAwesome else Icons.Default.AutoAwesomeOutlined,
                        contentDescription = "AI Mode",
                        tint = if (playerState.isAiModeEnabled) ElectricBlue else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ProgressBar(
    currentPosition: Long,
    duration: Long,
    onSeek: (Long) -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isUserSeeking by remember { mutableStateOf(false) }
    
    LaunchedEffect(currentPosition) {
        if (!isUserSeeking) {
            sliderPosition = if (duration > 0) {
                (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
        }
    }
    
    Column {
        Slider(
            value = sliderPosition,
            onValueChange = {
                isUserSeeking = true
                sliderPosition = it
            },
            onValueChangeFinished = {
                isUserSeeking = false
                onSeek((sliderPosition * duration).toLong())
            },
            colors = SliderDefaults.colors(
                thumbColor = ElectricBlue,
                activeTrackColor = ElectricBlue,
                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
            )
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(currentPosition),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = formatTime(duration),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun PlaybackControls(
    isPlaying: Boolean,
    isShuffleEnabled: Boolean,
    repeatMode: org.roshan.beats.viewmodel.RepeatMode,
    onPlayPauseClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onRepeatClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Shuffle
        IconButton(onClick = onShuffleClick) {
            Icon(
                imageVector = Icons.Default.Shuffle,
                contentDescription = "Shuffle",
                tint = if (isShuffleEnabled) ElectricBlue else Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(28.dp)
            )
        }
        
        // Previous
        IconButton(
            onClick = onPreviousClick,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "Previous",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
        
        // Play/Pause
        FilledIconButton(
            onClick = onPlayPauseClick,
            modifier = Modifier.size(72.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = ElectricBlue
            )
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
        
        // Next
        IconButton(
            onClick = onNextClick,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "Next",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
        
        // Repeat
        IconButton(onClick = onRepeatClick) {
            Icon(
                imageVector = when (repeatMode) {
                    RepeatMode.OFF -> Icons.Default.Repeat
                    RepeatMode.ALL -> Icons.Default.Repeat
                    RepeatMode.ONE -> Icons.Default.RepeatOne
                },
                contentDescription = "Repeat",
                tint = if (repeatMode != RepeatMode.OFF) ElectricBlue else Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

private fun formatTime(millis: Long): String {
    val seconds = (millis / 1000).toInt()
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%d:%02d".format(minutes, remainingSeconds)
}
