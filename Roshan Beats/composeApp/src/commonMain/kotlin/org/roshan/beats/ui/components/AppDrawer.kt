package org.roshan.beats.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.roshan.beats.ui.theme.ElectricBlue
import org.roshan.beats.ui.theme.ElectricViolet

data class DrawerMenuItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val badge: String? = null,
    val section: DrawerSection = DrawerSection.MAIN
)

enum class DrawerSection {
    MAIN, LIBRARY, DISCOVER, TOOLS, SETTINGS
}

@Composable
fun AppDrawer(
    onMenuItemClick: (String) -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val menuItems = remember {
        listOf(
            // Main Section
            DrawerMenuItem("home", "Home", Icons.Default.Home, section = DrawerSection.MAIN),
            DrawerMenuItem("trending", "Trending", Icons.Default.TrendingUp, section = DrawerSection.MAIN),
            DrawerMenuItem("new_releases", "New Releases", Icons.Default.NewReleases, section = DrawerSection.MAIN),
            DrawerMenuItem("charts", "Top Charts", Icons.Default.BarChart, section = DrawerSection.MAIN),
            
            // Library Section
            DrawerMenuItem("library", "Your Library", Icons.Default.LibraryMusic, section = DrawerSection.LIBRARY),
            DrawerMenuItem("liked_songs", "Liked Songs", Icons.Default.Favorite, "∞", DrawerSection.LIBRARY),
            DrawerMenuItem("playlists", "Playlists", Icons.Default.QueueMusic, section = DrawerSection.LIBRARY),
            DrawerMenuItem("albums", "Albums", Icons.Default.Album, section = DrawerSection.LIBRARY),
            DrawerMenuItem("artists", "Artists", Icons.Default.Person, section = DrawerSection.LIBRARY),
            DrawerMenuItem("downloads", "Downloads", Icons.Default.Download, section = DrawerSection.LIBRARY),
            DrawerMenuItem("history", "History", Icons.Default.History, section = DrawerSection.LIBRARY),
            
            // Discover Section
            DrawerMenuItem("genres", "Genres", Icons.Default.Category, section = DrawerSection.DISCOVER),
            DrawerMenuItem("moods", "Moods & Activities", Icons.Default.Mood, section = DrawerSection.DISCOVER),
            DrawerMenuItem("radio", "Radio Stations", Icons.Default.Radio, section = DrawerSection.DISCOVER),
            DrawerMenuItem("podcasts", "Podcasts", Icons.Default.Podcast, section = DrawerSection.DISCOVER),
            DrawerMenuItem("concerts", "Concerts & Events", Icons.Default.Event, section = DrawerSection.DISCOVER),
            DrawerMenuItem("year_wrapped", "Your Year Wrapped", Icons.Default.Celebration, section = DrawerSection.DISCOVER),
            
            // Tools Section
            DrawerMenuItem("equalizer", "Equalizer", Icons.Default.Equalizer, section = DrawerSection.TOOLS),
            DrawerMenuItem("sleep_timer", "Sleep Timer", Icons.Default.NightlightRound, section = DrawerSection.TOOLS),
            DrawerMenuItem("visualizer", "Audio Visualizer", Icons.Default.GraphicEq, section = DrawerSection.TOOLS),
            DrawerMenuItem("car_mode", "Car Mode", Icons.Default.DirectionsCar, section = DrawerSection.TOOLS),
            DrawerMenuItem("casting", "Cast to Device", Icons.Default.Cast, section = DrawerSection.TOOLS),
            DrawerMenuItem("statistics", "Listening Stats", Icons.Default.Analytics, section = DrawerSection.TOOLS),
            
            // Settings Section
            DrawerMenuItem("settings", "Settings", Icons.Default.Settings, section = DrawerSection.SETTINGS),
            DrawerMenuItem("storage", "Storage Manager", Icons.Default.Storage, section = DrawerSection.SETTINGS),
            DrawerMenuItem("about", "About", Icons.Default.Info, section = DrawerSection.SETTINGS)
        )
    }
    
    // Pulsing animation for featured items
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    ModalDrawerSheet(
        modifier = modifier.width(320.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            // Header
            item {
                DrawerHeader(pulse)
            }
            
            // Main Section
            item {
                DrawerSectionHeader("EXPLORE")
            }
            items(menuItems.filter { it.section == DrawerSection.MAIN }.size) { index ->
                val item = menuItems.filter { it.section == DrawerSection.MAIN }[index]
                DrawerItem(
                    item = item,
                    onClick = {
                        onMenuItemClick(item.id)
                        onCloseDrawer()
                    }
                )
            }
            
            // Library Section
            item {
                DrawerSectionHeader("YOUR LIBRARY")
            }
            items(menuItems.filter { it.section == DrawerSection.LIBRARY }.size) { index ->
                val item = menuItems.filter { it.section == DrawerSection.LIBRARY }[index]
                DrawerItem(
                    item = item,
                    onClick = {
                        onMenuItemClick(item.id)
                        onCloseDrawer()
                    }
                )
            }
            
            // Discover Section
            item {
                DrawerSectionHeader("DISCOVER")
            }
            items(menuItems.filter { it.section == DrawerSection.DISCOVER }.size) { index ->
                val item = menuItems.filter { it.section == DrawerSection.DISCOVER }[index]
                DrawerItem(
                    item = item,
                    onClick = {
                        onMenuItemClick(item.id)
                        onCloseDrawer()
                    }
                )
            }
            
            // Tools Section
            item {
                DrawerSectionHeader("TOOLS")
            }
            items(menuItems.filter { it.section == DrawerSection.TOOLS }.size) { index ->
                val item = menuItems.filter { it.section == DrawerSection.TOOLS }[index]
                DrawerItem(
                    item = item,
                    onClick = {
                        onMenuItemClick(item.id)
                        onCloseDrawer()
                    }
                )
            }
            
            // Settings Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(menuItems.filter { it.section == DrawerSection.SETTINGS }.size) { index ->
                val item = menuItems.filter { it.section == DrawerSection.SETTINGS }[index]
                DrawerItem(
                    item = item,
                    onClick = {
                        onMenuItemClick(item.id)
                        onCloseDrawer()
                    }
                )
            }
            
            // Footer
            item {
                Spacer(modifier = Modifier.height(16.dp))
                DrawerFooter()
            }
        }
    }
}

@Composable
private fun DrawerHeader(pulse: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ElectricBlue.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(pulse)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(ElectricBlue, ElectricViolet)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Roshan Beats",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "Feel the Future of Sound",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DrawerSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
private fun DrawerItem(
    item: DrawerMenuItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            
            item.badge?.let { badge ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Version 1.0.0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Created by Roshan",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
