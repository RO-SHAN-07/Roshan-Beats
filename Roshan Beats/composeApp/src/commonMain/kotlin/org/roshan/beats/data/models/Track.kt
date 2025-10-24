package org.roshan.beats.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Track(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("artist_name")
    val artistName: String,
    @SerialName("artist_id")
    val artistId: String? = null,
    @SerialName("album_name")
    val albumName: String = "",
    @SerialName("album_id")
    val albumId: String? = null,
    @SerialName("album_image")
    val albumImage: String? = null,
    @SerialName("duration")
    val duration: Int = 0,
    @SerialName("audio")
    val audio: String? = null,
    @SerialName("audiodownload")
    val audioDownload: String? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("releasedate")
    val releaseDate: String? = null,
    @SerialName("position")
    val position: Int? = null,
    val tags: List<String> = emptyList(),
    val isLiked: Boolean = false,
    val isDownloaded: Boolean = false,
    val localPath: String? = null
) {
    val durationFormatted: String
        get() {
            val minutes = duration / 60
            val seconds = duration % 60
            return "%d:%02d".format(minutes, seconds)
        }
    
    val audioUrl: String
        get() = audio ?: audioDownload ?: ""
    
    val coverArt: String
        get() = albumImage ?: image ?: ""
}
