package org.roshan.beats.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.roshan.beats.ui.theme.*

data class EqualizerBand(
    val frequency: String,
    val value: Float
)

data class EqualizerPreset(
    val name: String,
    val icon: String,
    val bands: List<Float>
)

@Composable
fun EqualizerScreen(
    modifier: Modifier = Modifier
) {
    var selectedPreset by remember { mutableStateOf<String?>("Flat") }
    var bands by remember {
        mutableStateOf(
            listOf(
                EqualizerBand("60Hz", 0f),
                EqualizerBand("230Hz", 0f),
                EqualizerBand("910Hz", 0f),
                EqualizerBand("3.6kHz", 0f),
                EqualizerBand("14kHz", 0f)
            )
        )
    }
    
    val presets = remember {
        listOf(
            EqualizerPreset("Flat", "📊", listOf(0f, 0f, 0f, 0f, 0f)),
            EqualizerPreset("Bass Boost", "🔊", listOf(8f, 5f, 2f, 0f, -2f)),
            EqualizerPreset("Treble Boost", "🎧", listOf(-2f, 0f, 2f, 5f, 8f)),
            EqualizerPreset("Rock", "🎸", listOf(5f, 3f, -2f, 2f, 5f)),
            EqualizerPreset("Pop", "🎤", listOf(2f, 4f, 3f, 2f, 1f)),
            EqualizerPreset("Jazz", "🎺", listOf(3f, 2f, 0f, 2f, 4f)),
            EqualizerPreset("Classical", "🎻", listOf(3f, 2f, -1f, 2f, 3f)),
            EqualizerPreset("Vocal Boost", "🎙️", listOf(-2f, 0f, 5f, 3f, 0f)),
            EqualizerPreset("Electronic", "🎹", listOf(6f, 3f, 0f, 2f, 5f))
        )
    }
    
    var bassBoost by remember { mutableFloatStateOf(0f) }
    var virtualizer by remember { mutableFloatStateOf(0f) }
    var loudness by remember { mutableStateOf(false) }
    
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Title
            item {
                Text(
                    text = "Audio Equalizer",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Presets
            item {
                Column {
                    Text(
                        text = "Presets",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presets.take(4).forEach { preset ->
                            PresetChip(
                                preset = preset,
                                isSelected = selectedPreset == preset.name,
                                onClick = {
                                    selectedPreset = preset.name
                                    bands = bands.mapIndexed { index, band ->
                                        band.copy(value = preset.bands[index])
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            
            // More presets
            items(presets.drop(4).chunked(4)) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { preset ->
                        PresetChip(
                            preset = preset,
                            isSelected = selectedPreset == preset.name,
                            onClick = {
                                selectedPreset = preset.name
                                bands = bands.mapIndexed { index, band ->
                                    band.copy(value = preset.bands[index])
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Fill remaining space if row has less than 4 items
                    repeat(4 - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            
            // Visualizer
            item {
                EqualizerVisualizer(bands = bands)
            }
            
            // EQ Bands
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "5-Band Equalizer",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        bands.forEachIndexed { index, band ->
                            EqualizerBandControl(
                                band = band,
                                onValueChange = { newValue ->
                                    bands = bands.toMutableList().apply {
                                        this[index] = band.copy(value = newValue)
                                    }
                                    selectedPreset = "Custom"
                                }
                            )
                            if (index < bands.size - 1) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
            
            // Audio Effects
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Audio Effects",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        // Bass Boost
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Bass Boost",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${(bassBoost * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Slider(
                                value = bassBoost,
                                onValueChange = { bassBoost = it },
                                colors = SliderDefaults.colors(
                                    thumbColor = ElectricBlue,
                                    activeTrackColor = ElectricBlue
                                )
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Virtualizer
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Surround Sound",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${(virtualizer * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Slider(
                                value = virtualizer,
                                onValueChange = { virtualizer = it },
                                colors = SliderDefaults.colors(
                                    thumbColor = ElectricViolet,
                                    activeTrackColor = ElectricViolet
                                )
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Loudness Enhancer
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Loudness Enhancer",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Boost overall volume",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = loudness,
                                onCheckedChange = { loudness = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ElectricBlue,
                                    checkedTrackColor = ElectricBlue.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PresetChip(
    preset: EqualizerPreset,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(preset.icon, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = preset.name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        },
        leadingIcon = if (isSelected) {
            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
        } else null,
        modifier = modifier
    )
}

@Composable
private fun EqualizerBandControl(
    band: EqualizerBand,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = band.frequency,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${if (band.value >= 0) "+" else ""}${band.value.toInt()} dB",
                style = MaterialTheme.typography.bodySmall,
                color = if (band.value > 0) ElectricBlue else if (band.value < 0) Color(0xFFFF5252) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Slider(
            value = band.value,
            onValueChange = onValueChange,
            valueRange = -12f..12f,
            colors = SliderDefaults.colors(
                thumbColor = ElectricBlue,
                activeTrackColor = ElectricBlue
            )
        )
    }
}

@Composable
private fun EqualizerVisualizer(bands: List<EqualizerBand>) {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedValues = bands.map { band ->
        infiniteTransition.animateFloat(
            initialValue = band.value * 0.8f,
            targetValue = band.value * 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = EaseInOut),
                repeatMode = RepeatMode.Reverse
            )
        )
    }
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp)
        ) {
            val barWidth = size.width / (bands.size * 2)
            val centerY = size.height / 2
            
            bands.forEachIndexed { index, band ->
                val x = (index * 2 + 1) * barWidth
                val animValue = animatedValues[index].value
                val barHeight = (animValue / 12f) * centerY * 0.8f
                
                // Draw bars from center
                drawLine(
                    brush = Brush.verticalGradient(
                        colors = listOf(ElectricBlue, ElectricViolet)
                    ),
                    start = Offset(x, centerY - barHeight),
                    end = Offset(x, centerY + barHeight),
                    strokeWidth = barWidth * 0.6f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}
