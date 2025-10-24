package org.roshan.beats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.roshan.beats.ui.components.BottomNavItem
import org.roshan.beats.ui.components.BottomNavigationBar
import org.roshan.beats.ui.components.MiniPlayer
import org.roshan.beats.ui.components.QueueModal
import org.roshan.beats.ui.navigation.Screen
import org.roshan.beats.ui.screens.*
import org.roshan.beats.ui.theme.RoshanBeatsTheme
import org.roshan.beats.viewmodel.MusicViewModel
import org.roshan.beats.viewmodel.PlayerViewModel

@Composable
fun App() {
    var isDarkTheme by remember { mutableStateOf(true) }
    val musicViewModel: MusicViewModel = viewModel { MusicViewModel() }
    val playerViewModel: PlayerViewModel = viewModel { PlayerViewModel() }
    
    RoshanBeatsTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()
        var showSplash by remember { mutableStateOf(true) }
        var showQueue by remember { mutableStateOf(false) }
        var selectedBottomNav by remember { mutableStateOf(BottomNavItem.HOME) }
        
        if (showQueue) {
            QueueModal(
                playerViewModel = playerViewModel,
                onDismiss = { showQueue = false }
            )
        }
        
        if (showSplash) {
            SplashScreen(
                onSplashComplete = { showSplash = false }
            )
        } else {
            Scaffold(
                bottomBar = {
                    Column {
                        MiniPlayer(
                            playerViewModel = playerViewModel,
                            onExpand = {
                                selectedBottomNav = BottomNavItem.PLAYER
                                navController.navigate(Screen.Player) {
                                    popUpTo(Screen.Home) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                        BottomNavigationBar(
                            selectedItem = selectedBottomNav,
                            onItemSelected = { item ->
                                selectedBottomNav = item
                                when (item) {
                                    BottomNavItem.HOME -> navController.navigate(Screen.Home) {
                                        popUpTo(Screen.Home) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                    BottomNavItem.SEARCH -> navController.navigate(Screen.Search) {
                                        popUpTo(Screen.Home) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    BottomNavItem.PLAYER -> navController.navigate(Screen.Player) {
                                        popUpTo(Screen.Home) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    BottomNavItem.LIBRARY -> navController.navigate(Screen.Library) {
                                        popUpTo(Screen.Home) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    BottomNavItem.SETTINGS -> navController.navigate(Screen.Settings) {
                                        popUpTo(Screen.Home) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable<Screen.Home> {
                        HomeScreen(
                            musicViewModel = musicViewModel,
                            playerViewModel = playerViewModel,
                            onTrackClick = { track ->
                                musicViewModel.addToRecentlyPlayed(track)
                                playerViewModel.playTrack(track)
                            },
                            onAlbumClick = { album ->
                                navController.navigate(Screen.AlbumDetail(album.id, album.name))
                            },
                            onArtistClick = { artist ->
                                navController.navigate(Screen.ArtistDetail(artist.id, artist.name))
                            }
                        )
                    }
                    
                    composable<Screen.Search> {
                        SearchScreen(
                            musicViewModel = musicViewModel,
                            onTrackClick = { track ->
                                musicViewModel.addToRecentlyPlayed(track)
                                playerViewModel.playTrack(track)
                            },
                            onAlbumClick = { album ->
                                navController.navigate(Screen.AlbumDetail(album.id, album.name))
                            },
                            onArtistClick = { artist ->
                                navController.navigate(Screen.ArtistDetail(artist.id, artist.name))
                            }
                        )
                    }
                    
                    composable<Screen.Player> {
                        PlayerScreen(
                            playerViewModel = playerViewModel,
                            onShowQueue = { showQueue = true }
                        )
                    }
                    
                    composable<Screen.Library> {
                        LibraryScreen(
                            musicViewModel = musicViewModel,
                            onTrackClick = { track ->
                                musicViewModel.addToRecentlyPlayed(track)
                                playerViewModel.playTrack(track)
                            },
                            onPlaylistClick = { playlist ->
                                navController.navigate(Screen.PlaylistDetail(playlist.id))
                            }
                        )
                    }
                    
                    composable<Screen.Settings> {
                        SettingsScreen(
                            playerViewModel = playerViewModel,
                            onThemeToggle = { isDarkTheme = !isDarkTheme },
                            isDarkTheme = isDarkTheme
                        )
                    }
                    
                    composable<Screen.AlbumDetail> { backStackEntry ->
                        val args = backStackEntry.toRoute<Screen.AlbumDetail>()
                        AlbumDetailScreen(
                            albumId = args.albumId,
                            albumName = args.albumName,
                            musicViewModel = musicViewModel,
                            playerViewModel = playerViewModel,
                            onBackClick = { navController.navigateUp() },
                            onTrackClick = { track ->
                                musicViewModel.addToRecentlyPlayed(track)
                            }
                        )
                    }
                    
                    composable<Screen.ArtistDetail> { backStackEntry ->
                        val args = backStackEntry.toRoute<Screen.ArtistDetail>()
                        ArtistDetailScreen(
                            artistId = args.artistId,
                            artistName = args.artistName,
                            musicViewModel = musicViewModel,
                            playerViewModel = playerViewModel,
                            onBackClick = { navController.navigateUp() },
                            onTrackClick = { track ->
                                musicViewModel.addToRecentlyPlayed(track)
                            },
                            onAlbumClick = { album ->
                                navController.navigate(Screen.AlbumDetail(album.id, album.name))
                            }
                        )
                    }
                    
                    composable<Screen.PlaylistDetail> { backStackEntry ->
                        val args = backStackEntry.toRoute<Screen.PlaylistDetail>()
                        PlaylistDetailScreen(
                            playlistId = args.playlistId,
                            musicViewModel = musicViewModel,
                            playerViewModel = playerViewModel,
                            onBackClick = { navController.navigateUp() },
                            onTrackClick = { track ->
                                musicViewModel.addToRecentlyPlayed(track)
                            }
                        )
                    }
                }
            }
        }
    }
}