package org.roshan.beats.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.roshan.beats.data.models.Album
import org.roshan.beats.data.models.Track
import org.roshan.beats.ui.components.AlbumCard
import org.roshan.beats.ui.components.TrackCard
import org.roshan.beats.ui.theme.DeepCharcoal
import org.roshan.beats.ui.theme.JetBlack
import org.roshan.beats.viewmodel.MusicViewModel
import org.roshan.beats.viewmodel.PlayerViewModel

@Composable
fun ArtistDetailScreen(
    artistId: String,
    artistName: String,
    musicViewModel: MusicViewModel,
    playerViewModel: PlayerViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit,
    onAlbumClick: (Album) -> Unit,
    modifier: Modifier = Modifier
) {
    var tracks by remember { mutableStateOf<List<Track>>(emptyList()) }
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(artistId) {
        scope.launch {
            tracks = musicViewModel.getArtistTracks(artistId)
            albums = musicViewModel.getArtistAlbums(artistId)
            isLoading = false
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
            item {
                Column {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                    
                    Text(
                        text = artistName,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = {
                            if (tracks.isNotEmpty()) {
                                playerViewModel.playQueue(tracks, 0)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 20.dp),
                        enabled = tracks.isNotEmpty()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Play Popular Tracks")
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    TabRow(selectedTabIndex = selectedTab) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Top Songs") }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Albums") }
                        )
                    }
                }
            }
            
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                when (selectedTab) {
                    0 -> {
                        items(tracks) { track ->
                            TrackCard(
                                track = track,
                                isLiked = musicViewModel.isLiked(track.id),
                                onClick = { onTrackClick(track) },
                                onLikeClick = { musicViewModel.toggleLike(track) },
                                onMoreClick = {},
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                            )
                        }
                    }
                    1 -> {
                        items(albums) { album ->
                            AlbumCard(
                                album = album,
                                onClick = { onAlbumClick(album) },
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
