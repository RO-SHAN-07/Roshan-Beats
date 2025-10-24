package org.roshan.beats.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.roshan.beats.ui.theme.*

@Composable
fun YearWrappedScreen(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing)
        )
    )
    
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ElectricViolet.copy(alpha = 0.3f),
                        DeepCharcoal,
                        JetBlack
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Celebration,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .scale(pulse),
                        tint = ElectricBlue
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your 2024 Wrapped",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = ElectricBlue
                    )
                    Text(
                        text = "A year of amazing music",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Total listening time
            item {
                WrappedCard(
                    title = "Total Listening Time",
                    value = "12,847 minutes",
                    description = "That's 214 hours of pure music!",
                    icon = Icons.Default.Schedule,
                    gradient = listOf(ElectricBlue, VividBlue)
                )
            }
            
            // Top artist
            item {
                WrappedCard(
                    title = "Your #1 Artist",
                    value = "The Weeknd",
                    description = "You listened to 347 songs",
                    icon = Icons.Default.Person,
                    gradient = listOf(ElectricViolet, DeepViolet)
                )
            }
            
            // Top song
            item {
                WrappedCard(
                    title = "Most Played Song",
                    value = "Blinding Lights",
                    description = "Played 127 times this year",
                    icon = Icons.Default.MusicNote,
                    gradient = listOf(NeonPurple, ElectricViolet)
                )
            }
            
            // Top genre
            item {
                WrappedCard(
                    title = "Favorite Genre",
                    value = "Electronic",
                    description = "45% of your listening",
                    icon = Icons.Default.Category,
                    gradient = listOf(NeonBlue, ElectricBlue)
                )
            }
            
            // Discovery
            item {
                WrappedCard(
                    title = "New Artists Discovered",
                    value = "89 Artists",
                    description = "You love exploring new music!",
                    icon = Icons.Default.Explore,
                    gradient = listOf(Color(0xFFFF5252), Color(0xFFD32F2F))
                )
            }
            
            // Listening hours
            item {
                WrappedCard(
                    title = "Peak Listening Hour",
                    value = "11 PM",
                    description = "Night owl music lover 🦉",
                    icon = Icons.Default.Nightlight,
                    gradient = listOf(Color(0xFF7986CB), Color(0xFF5C6BC0))
                )
            }
            
            // Share button
            item {
                Button(
                    onClick = { /* Share wrapped */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricBlue
                    )
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Share Your Wrapped",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun WrappedCard(
    title: String,
    value: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: List<Color>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(gradient.map { it.copy(alpha = 0.3f) })
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(gradient)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.width(20.dp))
                
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
