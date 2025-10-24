package org.roshan.beats.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.roshan.beats.ui.theme.*

data class ListeningStats(
    val totalTime: String,
    val totalTracks: Int,
    val totalArtists: Int,
    val totalGenres: Int,
    val avgDailyTime: String,
    val longestStreak: Int
)

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier
) {
    val stats = remember {
        ListeningStats(
            totalTime = "127 hours",
            totalTracks = 1543,
            totalArtists = 234,
            totalGenres = 18,
            avgDailyTime = "2h 15m",
            longestStreak = 42
        )
    }
    
    val weeklyData = remember {
        listOf(45f, 62f, 38f, 75f, 55f, 90f, 68f)
    }
    
    val topGenres = remember {
        listOf(
            "Pop" to 35f,
            "Electronic" to 25f,
            "Rock" to 20f,
            "Hip Hop" to 12f,
            "Jazz" to 8f
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Your Listening Stats",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Overview cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        icon = Icons.Default.MusicNote,
                        value = stats.totalTracks.toString(),
                        label = "Tracks",
                        color = ElectricBlue,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = Icons.Default.Person,
                        value = stats.totalArtists.toString(),
                        label = "Artists",
                        color = ElectricViolet,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        icon = Icons.Default.Schedule,
                        value = stats.totalTime,
                        label = "Total Time",
                        color = NeonPurple,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = Icons.Default.LocalFireDepartment,
                        value = "${stats.longestStreak} days",
                        label = "Streak",
                        color = Color(0xFFFF5252),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            // Weekly chart
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "This Week",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        WeeklyChart(data = weeklyData)
                    }
                }
            }
            
            // Top genres
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Top Genres",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        topGenres.forEach { (genre, percentage) ->
                            GenreBar(genre = genre, percentage = percentage)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WeeklyChart(data: List<Float>) {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val maxValue = data.maxOrNull() ?: 1f
    
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        val barWidth = size.width / (data.size * 2)
        val maxHeight = size.height - 30.dp.toPx()
        
        data.forEachIndexed { index, value ->
            val barHeight = (value / maxValue) * maxHeight
            val x = (index * 2 + 1) * barWidth
            
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(ElectricBlue, ElectricViolet)
                ),
                topLeft = Offset(x - barWidth / 2, size.height - barHeight - 20.dp.toPx()),
                size = Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
            )
        }
    }
}

@Composable
private fun GenreBar(genre: String, percentage: Float) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = genre,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${percentage.toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = ElectricBlue
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = ElectricBlue,
            trackColor = MaterialTheme.colorScheme.surface
        )
    }
}
