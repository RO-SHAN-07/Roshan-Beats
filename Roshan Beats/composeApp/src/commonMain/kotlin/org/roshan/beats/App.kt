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
import kotlinx.coroutines.launch
import org.roshan.beats.ui.components.*
import org.roshan.beats.ui.navigation.Screen
import org.roshan.beats.ui.screens.*
import org.roshan.beats.ui.theme.RoshanBeatsTheme
import org.roshan.beats.viewmodel.MusicViewModel
import org.roshan.beats.viewmodel.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var isDarkTheme by remember { mutableStateOf(true) }
    val musicViewModel: MusicViewModel = viewModel { MusicViewModel() }
    val playerViewModel: PlayerViewModel = viewModel { PlayerViewModel() }
    
    RoshanBeatsTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
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
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    AppDrawer(
                        onMenuItemClick = { menuId ->
                            when (menuId) {
                                "home" -> navController.navigate(Screen.Home)
                                "genres" -> navController.navigate(Screen.Genres)
                                "moods" -> navController.navigate(Screen.MoodPlaylists)
                                "radio" -> navController.navigate(Screen.Radio)
                                "charts" -> navController.navigate(Screen.Charts)
                                "podcasts" -> navController.navigate(Screen.Podcasts)
                                "equalizer" -> navController.navigate(Screen.Equalizer)
                                "sleep_timer" -> navController.navigate(Screen.SleepTimer)
                                "visualizer" -> navController.navigate(Screen.Visualizer)
                                "car_mode" -> navController.navigate(Screen.CarMode)
                                "statistics" -> navController.navigate(Screen.Statistics)
                                "year_wrapped" -> navController.navigate(Screen.YearWrapped)
                                "history" -> navController.navigate(Screen.History)
                                "storage" -> navController.navigate(Screen.StorageManager)
                                "liked_songs" -> navController.navigate(Screen.LikedSongs)
                                "library" -> navController.navigate(Screen.Library)
                                "settings" -> navController.navigate(Screen.Settings)
                            }
                        },
                        onCloseDrawer = {
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            ) {
                Scaffold(
                    topBar = {
                        RoshanBeatsTopAppBar(
                            title = "Roshan Beats",
                            showBackButton = false,
                            showMenuButton = true,
                            onMenuClick = {
                                scope.launch { drawerState.open() }
                            },
                            onSearchClick = {
                                navController.navigate(Screen.Search)
                            }
                        )
                    },
                    bottomBar = {
                        Column {
                            MiniPlayer(
                                playerViewModel = playerViewModel,
                                onExpand = {
                                    selectedBottomNav = BottomNavItem.PLAYER
                                    navController.navigate(Screen.Player)
                                }
                            )
                            BottomNavigationBar(
                                selectedItem = selectedBottomNav,
                                onItemSelected = { item ->
                                    selectedBottomNav = item
                                    when (item) {
                                        BottomNavItem.HOME -> navController.navigate(Screen.Home)
                                        BottomNavItem.SEARCH -> navController.navigate(Screen.Search)
                                        BottomNavItem.PLAYER -> navController.navigate(Screen.Player)
                                        BottomNavItem.LIBRARY -> navController.navigate(Screen.Library)
                                        BottomNavItem.SETTINGS -> navController.navigate(Screen.Settings)
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
                            HomeScreen(musicViewModel, playerViewModel, { track ->
                                musicViewModel.addToRecentlyPlayed(track)
                                playerViewModel.playTrack(track)
                            }, { album ->
                                navController.navigate(Screen.AlbumDetail(album.id, album.name))
                            }, { artist ->
                                navController.navigate(Screen.ArtistDetail(artist.id, artist.name))
                            })
                        }
                        
                        composable<Screen.Search> {
                            SearchScreen(musicViewModel, { track ->
                                musicViewModel.addToRecentlyPlayed(track)
                                playerViewModel.playTrack(track)
                            }, { album ->
                                navController.navigate(Screen.AlbumDetail(album.id, album.name))
                            }, { artist ->
                                navController.navigate(Screen.ArtistDetail(artist.id, artist.name))
                            })
                        }
                        
                        composable<Screen.Player> {
                            PlayerScreen(playerViewModel, { showQueue = true })
                        }
                        
                        composable<Screen.Library> {
                            LibraryScreen(musicViewModel, { track ->
                                musicViewModel.addToRecentlyPlayed(track)
                                playerViewModel.playTrack(track)
                            }, { playlist ->
                                navController.navigate(Screen.PlaylistDetail(playlist.id))
                            })
                        }
                        
                        composable<Screen.Settings> {
                            SettingsScreen(playerViewModel, { isDarkTheme = !isDarkTheme }, isDarkTheme)
                        }
                        
                        composable<Screen.AlbumDetail> { backStackEntry ->
                            val args = backStackEntry.toRoute<Screen.AlbumDetail>()
                            AlbumDetailScreen(args.albumId, args.albumName, musicViewModel, playerViewModel, { navController.navigateUp() }, { track ->
                                musicViewModel.addToRecentlyPlayed(track)
                            })
                        }
                        
                        composable<Screen.ArtistDetail> { backStackEntry ->
                            val args = backStackEntry.toRoute<Screen.ArtistDetail>()
                            ArtistDetailScreen(args.artistId, args.artistName, musicViewModel, playerViewModel, { navController.navigateUp() }, { track ->
                                musicViewModel.addToRecentlyPlayed(track)
                            }, { album ->
                                navController.navigate(Screen.AlbumDetail(album.id, album.name))
                            })
                        }
                        
                        composable<Screen.PlaylistDetail> { backStackEntry ->
                            val args = backStackEntry.toRoute<Screen.PlaylistDetail>()
                            PlaylistDetailScreen(args.playlistId, musicViewModel, playerViewModel, { navController.navigateUp() }, { track ->
                                musicViewModel.addToRecentlyPlayed(track)
                            })
                        }
                        
                        // New screens
                        composable<Screen.Genres> { GenreBrowserScreen({}, Modifier) }
                        composable<Screen.MoodPlaylists> { MoodPlaylistsScreen({}, Modifier) }
                        composable<Screen.Radio> { RadioStationsScreen({}, Modifier) }
                        composable<Screen.Charts> { ChartsScreen(musicViewModel, {}, Modifier) }
                        composable<Screen.Equalizer> { EqualizerScreen(Modifier) }
                        composable<Screen.SleepTimer> { SleepTimerScreen(Modifier) }
                        composable<Screen.Statistics> { StatisticsScreen(Modifier) }
                        composable<Screen.YearWrapped> { YearWrappedScreen(Modifier) }
                        composable<Screen.CarMode> { CarModeScreen(playerViewModel, Modifier) }
                        composable<Screen.Visualizer> {
                            val playerState by playerViewModel.playerState.collectAsState()
                            AudioVisualizerScreen(playerState.currentTrack, playerState.isPlaying, Modifier)
                        }
                        composable<Screen.Podcasts> { PodcastsScreen({}, Modifier) }
                        composable<Screen.History> { HistoryScreen(musicViewModel, {}, Modifier) }
                        composable<Screen.StorageManager> { StorageManagerScreen(Modifier) }
                        composable<Screen.LikedSongs> {
                            val likedTracks by musicViewModel.likedTracks.collectAsState()
                            // Show liked songs list
                        }
                        composable<Screen.Lyrics> {
                            val playerState by playerViewModel.playerState.collectAsState()
                            LyricsScreen(playerState.currentTrack, playerState.currentPosition, Modifier)
                        }
                    }
                }
            }
        }
    }
}