package org.roshan.beats.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.roshan.beats.data.models.Album
import org.roshan.beats.data.models.Artist
import org.roshan.beats.data.models.Track
import org.roshan.beats.ui.components.AlbumCard
import org.roshan.beats.ui.components.ArtistCard
import org.roshan.beats.ui.components.TrackCard
import org.roshan.beats.ui.theme.DeepCharcoal
import org.roshan.beats.ui.theme.JetBlack
import org.roshan.beats.viewmodel.MusicUiState
import org.roshan.beats.viewmodel.MusicViewModel
import org.roshan.beats.viewmodel.PlayerViewModel
import java.time.LocalTime

@Composable
fun HomeScreen(
    musicViewModel: MusicViewModel,
    playerViewModel: PlayerViewModel,
    onTrackClick: (Track) -> Unit,
    onAlbumClick: (Album) -> Unit,
    onArtistClick: (Artist) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by musicViewModel.uiState.collectAsState()
    val recentlyPlayed by musicViewModel.recentlyPlayed.collectAsState()
    val scope = rememberCoroutineScope()
    
    var chillMixes by remember { mutableStateOf<List<Track>>(emptyList()) }
    var focusMixes by remember { mutableStateOf<List<Track>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        scope.launch {
            chillMixes = musicViewModel.getChillMixes()
            focusMixes = musicViewModel.getFocusMixes()
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepCharcoal, JetBlack)
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Text(
                        text = "Roshan Beats",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = getGreeting(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            // Trending Now Section
            if (uiState.trendingTracks.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Trending Now",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.trendingTracks.take(10)) { track ->
                            TrackCard(
                                track = track,
                                isLiked = musicViewModel.isLiked(track.id),
                                onClick = { onTrackClick(track) },
                                onLikeClick = { musicViewModel.toggleLike(track) },
                                onMoreClick = { /* TODO: Show context menu */ },
                                modifier = Modifier.width(300.dp)
                            )
                        }
                    }
                }
            }
            
            // Recently Played Section
            if (recentlyPlayed.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Recently Played",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                    )
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recentlyPlayed.take(10)) { track ->
                            TrackCard(
                                track = track,
                                isLiked = musicViewModel.isLiked(track.id),
                                onClick = { onTrackClick(track) },
                                onLikeClick = { musicViewModel.toggleLike(track) },
                                onMoreClick = { /* TODO: Show context menu */ },
                                modifier = Modifier.width(300.dp)
                            )
                        }
                    }
                }
            }
            
            // Top Artists Section
            if (uiState.topArtists.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Top Artists",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                    )
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.topArtists) { artist ->
                            ArtistCard(
                                artist = artist,
                                onClick = { onArtistClick(artist) }
                            )
                        }
                    }
                }
            }
            
            // New Releases Section
            if (uiState.newReleases.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "New Releases",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                    )
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.newReleases) { album ->
                            AlbumCard(
                                album = album,
                                onClick = { onAlbumClick(album) }
                            )
                        }
                    }
                }
            }
            
            // Chill & Focus Mixes Section
            if (chillMixes.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Chill & Focus Mixes",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                    )
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(chillMixes.take(10)) { track ->
                            TrackCard(
                                track = track,
                                isLiked = musicViewModel.isLiked(track.id),
                                onClick = { onTrackClick(track) },
                                onLikeClick = { musicViewModel.toggleLike(track) },
                                onMoreClick = { /* TODO: Show context menu */ },
                                modifier = Modifier.width(300.dp)
                            )
                        }
                    }
                }
            }
            
            // Loading indicator
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            // Error message
            uiState.error?.let { error ->
                item {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

private fun getGreeting(): String {
    val hour = try {
        LocalTime.now().hour
    } catch (e: Exception) {
        12 // Default to noon if time cannot be determined
    }
    
    return when (hour) {
        in 0..11 -> "Good Morning, Roshan"
        in 12..16 -> "Good Afternoon, Roshan"
        in 17..20 -> "Good Evening, Roshan"
        else -> "Good Night, Roshan"
    }
}
