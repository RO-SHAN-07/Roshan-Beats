package org.roshan.beats.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.roshan.beats.data.models.Track
import org.roshan.beats.ui.components.TrackCard
import org.roshan.beats.ui.theme.*
import org.roshan.beats.viewmodel.MusicViewModel

@Composable
fun HistoryScreen(
    musicViewModel: MusicViewModel,
    onTrackClick: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    val recentlyPlayed by musicViewModel.recentlyPlayed.collectAsState()
    
    var selectedFilter by remember { mutableStateOf("All Time") }
    val filters = listOf("Today", "This Week", "This Month", "All Time")
    
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
            // Header
            Text(
                text = "Listening History",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp)
            )
            
            // Filter chips
            ScrollableTabRow(
                selectedTabIndex = filters.indexOf(selectedFilter),
                containerColor = MaterialTheme.colorScheme.surface,
                edgePadding = 20.dp
            ) {
                filters.forEach { filter ->
                    Tab(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        text = {
                            Text(
                                text = filter,
                                fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
            
            // History list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (recentlyPlayed.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Text(
                                text = "No listening history yet",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(recentlyPlayed) { track ->
                        TrackCard(
                            track = track,
                            isLiked = musicViewModel.isLiked(track.id),
                            onClick = { onTrackClick(track) },
                            onLikeClick = { musicViewModel.toggleLike(track) },
                            onMoreClick = {}
                        )
                    }
                }
            }
        }
    }
}
