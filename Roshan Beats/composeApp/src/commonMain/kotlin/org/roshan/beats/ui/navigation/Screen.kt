package org.roshan.beats.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Splash : Screen()
    
    @Serializable
    data object Home : Screen()
    
    @Serializable
    data object Search : Screen()
    
    @Serializable
    data object Player : Screen()
    
    @Serializable
    data object Library : Screen()
    
    @Serializable
    data object Settings : Screen()
    
    @Serializable
    data class AlbumDetail(val albumId: String, val albumName: String) : Screen()
    
    @Serializable
    data class ArtistDetail(val artistId: String, val artistName: String) : Screen()
    
    @Serializable
    data class PlaylistDetail(val playlistId: String) : Screen()
    
    // New Screens
    @Serializable
    data object Genres : Screen()
    
    @Serializable
    data object MoodPlaylists : Screen()
    
    @Serializable
    data object Radio : Screen()
    
    @Serializable
    data object Charts : Screen()
    
    @Serializable
    data object Equalizer : Screen()
    
    @Serializable
    data object SleepTimer : Screen()
    
    @Serializable
    data object Lyrics : Screen()
    
    @Serializable
    data object Statistics : Screen()
    
    @Serializable
    data object YearWrapped : Screen()
    
    @Serializable
    data object CarMode : Screen()
    
    @Serializable
    data object Visualizer : Screen()
    
    @Serializable
    data object Podcasts : Screen()
    
    @Serializable
    data object History : Screen()
    
    @Serializable
    data object StorageManager : Screen()
    
    @Serializable
    data object LikedSongs : Screen()
    
    @Serializable
    data object Downloads : Screen()
    
    @Serializable
    data object Trending : Screen()
    
    @Serializable
    data object NewReleases : Screen()
}
