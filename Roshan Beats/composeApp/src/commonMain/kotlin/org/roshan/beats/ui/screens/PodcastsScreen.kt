package org.roshan.beats.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.roshan.beats.ui.theme.*

data class Podcast(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val episodes: Int,
    val category: String,
    val color: Color
)

@Composable
fun PodcastsScreen(
    onPodcastClick: (Podcast) -> Unit,
    modifier: Modifier = Modifier
) {
    val podcasts = remember {
        listOf(
            Podcast("1", "Tech Talk Daily", "John Smith", "Latest tech news", 247, "Technology", ElectricBlue),
            Podcast("2", "Music History", "Sarah Johnson", "Stories behind the songs", 189, "Music", ElectricViolet),
            Podcast("3", "Mindful Minutes", "Dr. Emma Lee", "Meditation and wellness", 156, "Health", Color(0xFF66BB6A)),
            Podcast("4", "Business Insights", "Mark Davis", "Entrepreneurship tips", 312, "Business", Color(0xFFFFB74D)),
            Podcast("5", "True Crime Stories", "Lisa Brown", "Unsolved mysteries", 98, "Crime", Color(0xFFFF5252)),
            Podcast("6", "Comedy Hour", "Mike Wilson", "Stand-up and laughs", 203, "Comedy", Color(0xFFFFCA28)),
            Podcast("7", "Science Explained", "Prof. Alan Gray", "Complex topics made simple", 145, "Science", Color(0xFF42A5F5)),
            Podcast("8", "Sports Talk", "Chris Martin", "Weekly sports analysis", 278, "Sports", Color(0xFF26A69A))
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
                    text = "Popular Podcasts",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            items(podcasts) { podcast ->
                PodcastCard(
                    podcast = podcast,
                    onClick = { onPodcastClick(podcast) }
                )
            }
        }
    }
}

@Composable
private fun PodcastCard(
    podcast: Podcast,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(podcast.color.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Podcast,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = podcast.color
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = podcast.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Chip(label = podcast.category)
                    Chip(label = "${podcast.episodes} episodes")
                }
            }
        }
    }
}

@Composable
private fun Chip(label: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
