package org.roshan.beats.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JamendoTracksResponse(
    @SerialName("headers")
    val headers: JamendoHeaders,
    @SerialName("results")
    val results: List<Track>
)

@Serializable
data class JamendoAlbumsResponse(
    @SerialName("headers")
    val headers: JamendoHeaders,
    @SerialName("results")
    val results: List<Album>
)

@Serializable
data class JamendoArtistsResponse(
    @SerialName("headers")
    val headers: JamendoHeaders,
    @SerialName("results")
    val results: List<Artist>
)

@Serializable
data class JamendoHeaders(
    @SerialName("status")
    val status: String,
    @SerialName("code")
    val code: Int,
    @SerialName("error_message")
    val errorMessage: String? = null,
    @SerialName("warnings")
    val warnings: String? = null,
    @SerialName("results_count")
    val resultsCount: Int
)
