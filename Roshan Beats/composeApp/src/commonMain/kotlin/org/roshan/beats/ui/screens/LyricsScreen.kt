package org.roshan.beats.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.roshan.beats.data.models.Track
import org.roshan.beats.ui.theme.*

data class LyricLine(
    val time: Long,
    val text: String
)

@Composable
fun LyricsScreen(
    track: Track?,
    currentPosition: Long,
    modifier: Modifier = Modifier
) {
    // Sample lyrics - in production, fetch from API
    val lyrics = remember {
        listOf(
            LyricLine(0, ""),
            LyricLine(5000, "Feel the rhythm"),
            LyricLine(8000, "Of the music"),
            LyricLine(11000, "In your soul"),
            LyricLine(15000, ""),
            LyricLine(18000, "Let it take you"),
            LyricLine(21000, "To places unknown"),
            LyricLine(25000, "Where dreams come alive"),
            LyricLine(30000, ""),
            LyricLine(33000, "Dance through the night"),
            LyricLine(36000, "Feel the light"),
            LyricLine(40000, "In every beat"),
            LyricLine(43000, "In every sound"),
            LyricLine(47000, ""),
            LyricLine(50000, "This is your moment"),
            LyricLine(54000, "This is your time"),
            LyricLine(58000, "Feel the future of sound"),
            LyricLine(63000, "")
        )
    }
    
    val currentLineIndex = lyrics.indexOfLast { it.time <= currentPosition }
    val listState = rememberLazyListState()
    
    LaunchedEffect(currentLineIndex) {
        if (currentLineIndex >= 0) {
            listState.animateScrollToItem(maxOf(0, currentLineIndex - 2))
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepCharcoal.copy(alpha = 0.9f),
                        JetBlack
                    )
                )
            )
    ) {
        if (track != null) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Track info header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = ElectricBlue
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = track.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = track.artistName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                // Lyrics
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    itemsIndexed(lyrics) { index, lyric ->
                        LyricLineItem(
                            lyric = lyric,
                            isCurrent = index == currentLineIndex,
                            isPast = index < currentLineIndex,
                            isFuture = index > currentLineIndex
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        } else {
            // No track playing
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No lyrics available",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun LyricLineItem(
    lyric: LyricLine,
    isCurrent: Boolean,
    isPast: Boolean,
    isFuture: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isCurrent) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    val alpha by animateFloatAsState(
        targetValue = when {
            isCurrent -> 1f
            isPast -> 0.4f
            else -> 0.6f
        },
        animationSpec = tween(300)
    )
    
    Text(
        text = lyric.text,
        fontSize = if (isCurrent) 28.sp else 22.sp,
        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
        color = if (isCurrent) ElectricBlue else MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .alpha(alpha)
    )
}
