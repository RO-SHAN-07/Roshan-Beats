package org.roshan.beats.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.roshan.beats.data.api.JamendoApi
import org.roshan.beats.data.models.*

class MusicRepository {
    private val api = JamendoApi()
    
    private val _likedTracks = MutableStateFlow<List<Track>>(emptyList())
    val likedTracks: StateFlow<List<Track>> = _likedTracks
    
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists
    
    private val _recentlyPlayed = MutableStateFlow<List<Track>>(emptyList())
    val recentlyPlayed: StateFlow<List<Track>> = _recentlyPlayed
    
    // Music Discovery
    suspend fun getTrendingTracks(limit: Int = 50): Result<List<Track>> {
        return api.getTracks(limit = limit, order = "popularity_week")
    }
    
    suspend fun getNewReleases(limit: Int = 50): Result<List<Album>> {
        return api.getAlbums(limit = limit, order = "releasedate_desc")
    }
    
    suspend fun getTopArtists(limit: Int = 50): Result<List<Artist>> {
        return api.getArtists(limit = limit, order = "popularity_week")
    }
    
    suspend fun searchTracks(query: String): Result<List<Track>> {
        return api.searchTracks(query)
    }
    
    suspend fun searchAlbums(query: String): Result<List<Album>> {
        return api.getAlbums(search = query)
    }
    
    suspend fun searchArtists(query: String): Result<List<Artist>> {
        return api.getArtists(search = query)
    }
    
    suspend fun getAlbumDetails(albumId: String): Result<List<Track>> {
        return api.getAlbumTracks(albumId)
    }
    
    suspend fun getArtistTracks(artistId: String): Result<List<Track>> {
        return api.getArtistTracks(artistId)
    }
    
    suspend fun getArtistAlbums(artistId: String): Result<List<Album>> {
        return api.getArtistAlbums(artistId)
    }
    
    suspend fun getChillMixes(): Result<List<Track>> {
        return api.getRadio(limit = 50, tags = "chillout+ambient+lounge")
    }
    
    suspend fun getFocusMixes(): Result<List<Track>> {
        return api.getRadio(limit = 50, tags = "classical+instrumental+piano")
    }
    
    // AI-based recommendations
    suspend fun getRecommendations(basedOn: Track): Result<List<Track>> {
        // Use tags from the current track to find similar music
        val tags = basedOn.tags.take(3).joinToString("+")
        return if (tags.isNotEmpty()) {
            api.getRadio(limit = 30, tags = tags)
        } else {
            api.getTracks(limit = 30, order = "popularity_week")
        }
    }
    
    // Library Management
    fun toggleLike(track: Track) {
        val currentLiked = _likedTracks.value.toMutableList()
        val existingTrack = currentLiked.find { it.id == track.id }
        if (existingTrack != null) {
            currentLiked.remove(existingTrack)
        } else {
            currentLiked.add(0, track.copy(isLiked = true))
        }
        _likedTracks.value = currentLiked
    }
    
    fun isLiked(trackId: String): Boolean {
        return _likedTracks.value.any { it.id == trackId }
    }
    
    fun addToRecentlyPlayed(track: Track) {
        val current = _recentlyPlayed.value.toMutableList()
        current.removeAll { it.id == track.id }
        current.add(0, track)
        if (current.size > 50) {
            current.removeLast()
        }
        _recentlyPlayed.value = current
    }
    
    // Playlist Management
    fun createPlaylist(name: String, description: String = ""): Playlist {
        val playlist = Playlist(
            id = System.currentTimeMillis().toString(),
            name = name,
            description = description,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        val current = _playlists.value.toMutableList()
        current.add(0, playlist)
        _playlists.value = current
        return playlist
    }
    
    fun addTrackToPlaylist(playlistId: String, track: Track) {
        val current = _playlists.value.toMutableList()
        val playlistIndex = current.indexOfFirst { it.id == playlistId }
        if (playlistIndex != -1) {
            val playlist = current[playlistIndex]
            val updatedTracks = playlist.tracks.toMutableList()
            if (!updatedTracks.any { it.id == track.id }) {
                updatedTracks.add(track)
                current[playlistIndex] = playlist.copy(
                    tracks = updatedTracks,
                    updatedAt = System.currentTimeMillis()
                )
                _playlists.value = current
            }
        }
    }
    
    fun removeTrackFromPlaylist(playlistId: String, trackId: String) {
        val current = _playlists.value.toMutableList()
        val playlistIndex = current.indexOfFirst { it.id == playlistId }
        if (playlistIndex != -1) {
            val playlist = current[playlistIndex]
            val updatedTracks = playlist.tracks.filter { it.id != trackId }
            current[playlistIndex] = playlist.copy(
                tracks = updatedTracks,
                updatedAt = System.currentTimeMillis()
            )
            _playlists.value = current
        }
    }
    
    fun deletePlaylist(playlistId: String) {
        _playlists.value = _playlists.value.filter { it.id != playlistId }
    }
    
    fun renamePlaylist(playlistId: String, newName: String) {
        val current = _playlists.value.toMutableList()
        val playlistIndex = current.indexOfFirst { it.id == playlistId }
        if (playlistIndex != -1) {
            current[playlistIndex] = current[playlistIndex].copy(
                name = newName,
                updatedAt = System.currentTimeMillis()
            )
            _playlists.value = current
        }
    }
}
