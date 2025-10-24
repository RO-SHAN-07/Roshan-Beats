package org.roshan.beats.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

data class MoodPlaylist(
    val id: String,
    val name: String,
    val description: String,
    val emoji: String,
    val color: Color,
    val tags: List<String> = emptyList()
)

@Composable
fun MoodPlaylistsScreen(
    onPlaylistClick: (MoodPlaylist) -> Unit,
    modifier: Modifier = Modifier
) {
    val moodCategories = remember {
        mapOf(
            "Energy Boost" to listOf(
                MoodPlaylist("workout", "Workout Beast", "High energy tracks", "💪", Color(0xFFFF5252), listOf("pop", "electronic")),
                MoodPlaylist("party", "Party Mode", "Get the party started", "🎉", ElectricViolet),
                MoodPlaylist("motivation", "Motivation Station", "Push your limits", "⚡", ElectricBlue),
                MoodPlaylist("dance", "Dance Floor", "Move your body", "💃", NeonPurple)
            ),
            "Focus & Productivity" to listOf(
                MoodPlaylist("focus", "Deep Focus", "Zero distractions", "🎯", Color(0xFF42A5F5)),
                MoodPlaylist("study", "Study Session", "Concentration music", "📚", Color(0xFF66BB6A)),
                MoodPlaylist("coding", "Code Zone", "For programmers", "💻", Color(0xFF9575CD)),
                MoodPlaylist("work", "Work Flow", "Productive vibes", "💼", Color(0xFF78909C))
            ),
            "Chill & Relax" to listOf(
                MoodPlaylist("chill", "Chill Vibes", "Lay back and relax", "😌", Color(0xFF80DEEA)),
                MoodPlaylist("sleep", "Sleep Sounds", "Peaceful dreams", "😴", Color(0xFF7986CB)),
                MoodPlaylist("meditation", "Meditation", "Inner peace", "🧘", Color(0xFFA1887F)),
                MoodPlaylist("ambient", "Ambient Clouds", "Atmospheric sounds", "☁️", Color(0xFFB0BEC5))
            ),
            "Mood Boosters" to listOf(
                MoodPlaylist("happy", "Happy Hits", "Feel good music", "😄", Color(0xFFFFCA28)),
                MoodPlaylist("romance", "Romantic Mood", "Love songs", "❤️", Color(0xFFEC407A)),
                MoodPlaylist("nostalgia", "Throwback", "Good old days", "📼", Color(0xFFFF7043)),
                MoodPlaylist("confidence", "Confidence", "You got this", "👑", Color(0xFFFFD54F))
            ),
            "Activities" to listOf(
                MoodPlaylist("driving", "Road Trip", "Drive time", "🚗", Color(0xFF26A69A)),
                MoodPlaylist("cooking", "Cooking Beats", "Kitchen vibes", "🍳", Color(0xFFFFA726)),
                MoodPlaylist("gaming", "Gaming Mode", "Level up", "🎮", Color(0xFF5C6BC0)),
                MoodPlaylist("reading", "Reading Corner", "Book companions", "📖", Color(0xFF8D6E63))
            ),
            "Special Moments" to listOf(
                MoodPlaylist("morning", "Morning Energy", "Start your day", "🌅", Color(0xFFFFB74D)),
                MoodPlaylist("evening", "Evening Wind Down", "Sunset vibes", "🌆", Color(0xFFBA68C8)),
                MoodPlaylist("rainy", "Rainy Day", "Cozy weather", "🌧️", Color(0xFF78909C)),
                MoodPlaylist("travel", "Travel Tunes", "Adventure awaits", "✈️", Color(0xFF29B6F6))
            )
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
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 0.dp)
        ) {
            moodCategories.forEach { (category, playlists) ->
                item {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                }
                
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(playlists) { playlist ->
                            MoodPlaylistCard(
                                playlist = playlist,
                                onClick = { onPlaylistClick(playlist) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun MoodPlaylistCard(
    playlist: MoodPlaylist,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            playlist.color.copy(alpha = shimmer),
                            playlist.color.copy(alpha = 0.7f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = playlist.emoji,
                    style = MaterialTheme.typography.displaySmall
                )
                
                Column {
                    Text(
                        text = playlist.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = playlist.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
