package org.roshan.beats.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.roshan.beats.data.models.Track
import org.roshan.beats.ui.theme.*
import org.roshan.beats.viewmodel.MusicViewModel

enum class ChartType {
    TOP_50, NEW_ENTRIES, TRENDING, VIRAL
}

@Composable
fun ChartsScreen(
    musicViewModel: MusicViewModel,
    onTrackClick: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedChart by remember { mutableStateOf(ChartType.TOP_50) }
    val uiState by musicViewModel.uiState.collectAsState()
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepCharcoal, JetBlack)
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Chart type selector
            ScrollableTabRow(
                selectedTabIndex = selectedChart.ordinal,
                containerColor = MaterialTheme.colorScheme.surface,
                edgePadding = 16.dp
            ) {
                ChartType.entries.forEach { type ->
                    Tab(
                        selected = selectedChart == type,
                        onClick = { selectedChart = type },
                        text = {
                            Text(
                                text = when (type) {
                                    ChartType.TOP_50 -> "Top 50"
                                    ChartType.NEW_ENTRIES -> "New Entries"
                                    ChartType.TRENDING -> "Trending"
                                    ChartType.VIRAL -> "Viral"
                                },
                                fontWeight = if (selectedChart == type) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
            
            // Chart content
            AnimatedContent(
                targetState = selectedChart,
                transitionSpec = {
                    fadeIn() + slideInHorizontally() togetherWith fadeOut() + slideOutHorizontally()
                }
            ) { chartType ->
                ChartList(
                    tracks = uiState.trendingTracks.take(50),
                    chartType = chartType,
                    onTrackClick = onTrackClick
                )
            }
        }
    }
}

@Composable
private fun ChartList(
    tracks: List<Track>,
    chartType: ChartType,
    onTrackClick: (Track) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(tracks) { index, track ->
            ChartTrackItem(
                track = track,
                position = index + 1,
                chartType = chartType,
                onClick = { onTrackClick(track) }
            )
        }
    }
}

@Composable
private fun ChartTrackItem(
    track: Track,
    position: Int,
    chartType: ChartType,
    onClick: () -> Unit
) {
    val positionColor = when {
        position == 1 -> Color(0xFFFFD700) // Gold
        position == 2 -> Color(0xFFC0C0C0) // Silver
        position == 3 -> Color(0xFFCD7F32) // Bronze
        else -> MaterialTheme.colorScheme.primary
    }
    
    val changeIndicator = when (chartType) {
        ChartType.TOP_50 -> if (position <= 10) "▲" else if (position > 40) "▼" else "—"
        ChartType.NEW_ENTRIES -> "NEW"
        ChartType.TRENDING -> "🔥"
        ChartType.VIRAL -> "⚡"
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Position number
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (position <= 3) positionColor.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.surface
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = position.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (position <= 3) positionColor else MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Album art
            AsyncImage(
                model = track.coverArt,
                contentDescription = track.name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Track info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Text(
                    text = track.artistName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
            
            // Change indicator
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when (chartType) {
                    ChartType.NEW_ENTRIES -> ElectricBlue.copy(alpha = 0.2f)
                    ChartType.TRENDING -> Color(0xFFFF5252).copy(alpha = 0.2f)
                    ChartType.VIRAL -> ElectricViolet.copy(alpha = 0.2f)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            ) {
                Text(
                    text = changeIndicator,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
