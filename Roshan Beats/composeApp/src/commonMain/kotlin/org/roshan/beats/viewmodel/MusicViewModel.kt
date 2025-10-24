package org.roshan.beats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.roshan.beats.data.models.*
import org.roshan.beats.data.repository.MusicRepository

data class MusicUiState(
    val trendingTracks: List<Track> = emptyList(),
    val newReleases: List<Album> = emptyList(),
    val topArtists: List<Artist> = emptyList(),
    val searchResults: SearchResults = SearchResults(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class SearchResults(
    val tracks: List<Track> = emptyList(),
    val albums: List<Album> = emptyList(),
    val artists: List<Artist> = emptyList()
)

class MusicViewModel : ViewModel() {
    private val repository = MusicRepository()
    
    private val _uiState = MutableStateFlow(MusicUiState())
    val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()
    
    val likedTracks = repository.likedTracks
    val playlists = repository.playlists
    val recentlyPlayed = repository.recentlyPlayed
    
    init {
        loadInitialData()
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val trending = repository.getTrendingTracks(50)
                val releases = repository.getNewReleases(30)
                val artists = repository.getTopArtists(30)
                
                _uiState.update {
                    it.copy(
                        trendingTracks = trending.getOrDefault(emptyList()),
                        newReleases = releases.getOrDefault(emptyList()),
                        topArtists = artists.getOrDefault(emptyList()),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load music: ${e.message}"
                    )
                }
            }
        }
    }
    
    fun search(query: String) {
        if (query.isBlank()) {
            _uiState.update {
                it.copy(searchResults = SearchResults())
            }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val tracks = repository.searchTracks(query)
                val albums = repository.searchAlbums(query)
                val artists = repository.searchArtists(query)
                
                _uiState.update {
                    it.copy(
                        searchResults = SearchResults(
                            tracks = tracks.getOrDefault(emptyList()),
                            albums = albums.getOrDefault(emptyList()),
                            artists = artists.getOrDefault(emptyList())
                        ),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Search failed: ${e.message}"
                    )
                }
            }
        }
    }
    
    fun toggleLike(track: Track) {
        repository.toggleLike(track)
    }
    
    fun isLiked(trackId: String): Boolean {
        return repository.isLiked(trackId)
    }
    
    fun addToRecentlyPlayed(track: Track) {
        repository.addToRecentlyPlayed(track)
    }
    
    fun createPlaylist(name: String, description: String = ""): Playlist {
        return repository.createPlaylist(name, description)
    }
    
    fun addTrackToPlaylist(playlistId: String, track: Track) {
        repository.addTrackToPlaylist(playlistId, track)
    }
    
    fun removeTrackFromPlaylist(playlistId: String, trackId: String) {
        repository.removeTrackFromPlaylist(playlistId, trackId)
    }
    
    fun deletePlaylist(playlistId: String) {
        repository.deletePlaylist(playlistId)
    }
    
    fun renamePlaylist(playlistId: String, newName: String) {
        repository.renamePlaylist(playlistId, newName)
    }
    
    suspend fun getAlbumTracks(albumId: String): List<Track> {
        return repository.getAlbumDetails(albumId).getOrDefault(emptyList())
    }
    
    suspend fun getArtistTracks(artistId: String): List<Track> {
        return repository.getArtistTracks(artistId).getOrDefault(emptyList())
    }
    
    suspend fun getArtistAlbums(artistId: String): List<Album> {
        return repository.getArtistAlbums(artistId).getOrDefault(emptyList())
    }
    
    suspend fun getChillMixes(): List<Track> {
        return repository.getChillMixes().getOrDefault(emptyList())
    }
    
    suspend fun getFocusMixes(): List<Track> {
        return repository.getFocusMixes().getOrDefault(emptyList())
    }
    
    suspend fun getRecommendations(track: Track): List<Track> {
        return repository.getRecommendations(track).getOrDefault(emptyList())
    }
    
    fun refresh() {
        loadInitialData()
    }
}
