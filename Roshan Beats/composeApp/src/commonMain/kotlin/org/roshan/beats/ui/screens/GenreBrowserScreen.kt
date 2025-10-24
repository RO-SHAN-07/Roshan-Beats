package org.roshan.beats.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

data class GenreItem(
    val id: String,
    val name: String,
    val color1: Color,
    val color2: Color,
    val icon: String = "🎵"
)

@Composable
fun GenreBrowserScreen(
    onGenreClick: (GenreItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val genres = remember {
        listOf(
            GenreItem("pop", "Pop", ElectricBlue, VividBlue, "🎤"),
            GenreItem("rock", "Rock", Color(0xFFFF5252), Color(0xFFD32F2F), "🎸"),
            GenreItem("hip_hop", "Hip Hop", ElectricViolet, DeepViolet, "🎧"),
            GenreItem("electronic", "Electronic", NeonBlue, ElectricBlue, "🎹"),
            GenreItem("jazz", "Jazz", Color(0xFFFFB74D), Color(0xFFF57C00), "🎺"),
            GenreItem("classical", "Classical", Color(0xFF90CAF9), Color(0xFF1976D2), "🎻"),
            GenreItem("indie", "Indie", NeonPurple, ElectricViolet, "🎼"),
            GenreItem("r_and_b", "R&B", Color(0xFFBA68C8), Color(0xFF8E24AA), "💜"),
            GenreItem("country", "Country", Color(0xFFFFD54F), Color(0xFFF57F17), "🤠"),
            GenreItem("latin", "Latin", Color(0xFFFF7043), Color(0xFFE64A19), "💃"),
            GenreItem("metal", "Metal", Color(0xFF757575), Color(0xFF212121), "🤘"),
            GenreItem("blues", "Blues", Color(0xFF42A5F5), Color(0xFF0D47A1), "🎷"),
            GenreItem("reggae", "Reggae", Color(0xFF66BB6A), Color(0xFF388E3C), "🌴"),
            GenreItem("soul", "Soul", Color(0xFFEF5350), Color(0xFFC62828), "❤️"),
            GenreItem("funk", "Funk", Color(0xFFAB47BC), Color(0xFF6A1B9A), "🕺"),
            GenreItem("ambient", "Ambient", Color(0xFF80DEEA), Color(0xFF0097A7), "🌊"),
            GenreItem("folk", "Folk", Color(0xFFA1887F), Color(0xFF5D4037), "🎶"),
            GenreItem("punk", "Punk", Color(0xFFFF4081), Color(0xFFC51162), "⚡"),
            GenreItem("disco", "Disco", Color(0xFFFFCA28), Color(0xFFF9A825), "✨"),
            GenreItem("gospel", "Gospel", Color(0xFF9575CD), Color(0xFF512DA8), "🙏")
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp, bottom = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(genres) { genre ->
                GenreCard(
                    genre = genre,
                    onClick = { onGenreClick(genre) }
                )
            }
        }
    }
}

@Composable
private fun GenreCard(
    genre: GenreItem,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.3f)
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(genre.color1, genre.color2)
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
                    text = genre.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = genre.icon,
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
fun GenreDetailScreen(
    genre: GenreItem,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        genre.color1.copy(alpha = 0.3f),
                        DeepCharcoal,
                        JetBlack
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header section with genre info would go here
            // Track list for this genre would be displayed below
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(genre.color1, genre.color2)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = genre.icon,
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = genre.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
