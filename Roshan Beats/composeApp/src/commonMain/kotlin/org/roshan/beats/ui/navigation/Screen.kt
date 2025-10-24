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
}
