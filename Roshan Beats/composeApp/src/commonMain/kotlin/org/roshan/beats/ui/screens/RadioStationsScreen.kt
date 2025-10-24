package org.roshan.beats.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.roshan.beats.ui.theme.*

data class RadioStation(
    val id: String,
    val name: String,
    val description: String,
    val genre: String,
    val listeners: String,
    val isLive: Boolean = true,
    val color: Color
)

@Composable
fun RadioStationsScreen(
    onStationClick: (RadioStation) -> Unit,
    modifier: Modifier = Modifier
) {
    val stations = remember {
        listOf(
            RadioStation("1", "Hits Radio", "Today's biggest hits", "Pop", "2.5M", color = ElectricBlue),
            RadioStation("2", "Chill Beats", "Lo-fi & chill vibes", "Chill", "1.8M", color = Color(0xFF80DEEA)),
            RadioStation("3", "Rock Legends", "Classic rock anthems", "Rock", "1.5M", color = Color(0xFFFF5252)),
            RadioStation("4", "Electronic Waves", "EDM & House", "Electronic", "2.1M", color = NeonPurple),
            RadioStation("5", "Jazz Lounge", "Smooth jazz classics", "Jazz", "800K", color = Color(0xFFFFB74D)),
            RadioStation("6", "Hip Hop Nation", "Latest rap & hip hop", "Hip Hop", "3.2M", color = ElectricViolet),
            RadioStation("7", "Indie Discovery", "Discover new indie", "Indie", "1.2M", color = Color(0xFFA1887F)),
            RadioStation("8", "Classical Masterpieces", "Timeless classics", "Classical", "600K", color = Color(0xFF90CAF9)),
            RadioStation("9", "Country Roads", "Country music", "Country", "1.1M", color = Color(0xFFFFD54F)),
            RadioStation("10", "Latin Fever", "Reggaeton & Latin", "Latin", "2.7M", color = Color(0xFFFF7043))
        )
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
            contentPadding = PaddingValues(16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Live Radio Stations",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(stations) { station ->
                RadioStationCard(
                    station = station,
                    onClick = { onStationClick(station) }
                )
            }
        }
    }
}

@Composable
private fun RadioStationCard(
    station: RadioStation,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Station icon with live indicator
            Box {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(station.color, station.color.copy(alpha = 0.5f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                
                if (station.isLive) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .scale(pulse),
                        shape = CircleShape,
                        color = Color(0xFFFF5252)
                    ) {
                        Text(
                            text = "LIVE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = station.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Chip(label = station.genre, color = station.color)
                    Chip(label = "${station.listeners} listeners", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun Chip(label: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = color
        )
    }
}
