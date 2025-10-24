package org.roshan.beats.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Playlist(
    val id: String,
    val name: String,
    val description: String = "",
    val tracks: List<Track> = emptyList(),
    val coverImage: String? = null,
    val createdAt: Long = 0,
    val updatedAt: Long = 0
) {
    val trackCount: Int
        get() = tracks.size
    
    val totalDuration: Int
        get() = tracks.sumOf { it.duration }
    
    val totalDurationFormatted: String
        get() {
            val hours = totalDuration / 3600
            val minutes = (totalDuration % 3600) / 60
            return if (hours > 0) {
                "$hours hr $minutes min"
            } else {
                "$minutes min"
            }
        }
}
