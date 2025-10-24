package org.roshan.beats.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("artist_name")
    val artistName: String,
    @SerialName("artist_id")
    val artistId: String? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("releasedate")
    val releaseDate: String? = null,
    @SerialName("tracks")
    val tracks: List<Track> = emptyList(),
    val tags: List<String> = emptyList()
)
