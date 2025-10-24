package org.roshan.beats.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("image")
    val image: String? = null,
    @SerialName("website")
    val website: String? = null,
    val genres: List<String> = emptyList(),
    val followers: Int = 0
)
