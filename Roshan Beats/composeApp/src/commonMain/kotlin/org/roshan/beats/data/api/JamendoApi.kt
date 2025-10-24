package org.roshan.beats.data.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.roshan.beats.data.models.*

class JamendoApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
        }
    }

    private val baseUrl = "https://api.jamendo.com/v3.0"
    private val clientId = "56d30c95" // Public Jamendo API client ID

    suspend fun getTracks(
        limit: Int = 50,
        offset: Int = 0,
        order: String = "popularity_week",
        tags: String? = null,
        search: String? = null
    ): Result<List<Track>> {
        return try {
            val response: JamendoTracksResponse = client.get("$baseUrl/tracks") {
                parameter("client_id", clientId)
                parameter("format", "json")
                parameter("limit", limit)
                parameter("offset", offset)
                parameter("order", order)
                parameter("include", "musicinfo")
                parameter("imagesize", "600")
                tags?.let { parameter("tags", it) }
                search?.let { parameter("search", it) }
            }.body()
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchTracks(query: String, limit: Int = 50): Result<List<Track>> {
        return getTracks(limit = limit, search = query)
    }

    suspend fun getAlbums(
        limit: Int = 50,
        offset: Int = 0,
        order: String = "releasedate_desc",
        search: String? = null
    ): Result<List<Album>> {
        return try {
            val response: JamendoAlbumsResponse = client.get("$baseUrl/albums") {
                parameter("client_id", clientId)
                parameter("format", "json")
                parameter("limit", limit)
                parameter("offset", offset)
                parameter("order", order)
                parameter("imagesize", "600")
                search?.let { parameter("search", it) }
            }.body()
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbumTracks(albumId: String): Result<List<Track>> {
        return try {
            val response: JamendoTracksResponse = client.get("$baseUrl/tracks") {
                parameter("client_id", clientId)
                parameter("format", "json")
                parameter("album_id", albumId)
                parameter("include", "musicinfo")
                parameter("imagesize", "600")
                parameter("order", "track_position")
            }.body()
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArtists(
        limit: Int = 50,
        offset: Int = 0,
        order: String = "popularity_week",
        search: String? = null
    ): Result<List<Artist>> {
        return try {
            val response: JamendoArtistsResponse = client.get("$baseUrl/artists") {
                parameter("client_id", clientId)
                parameter("format", "json")
                parameter("limit", limit)
                parameter("offset", offset)
                parameter("order", order)
                parameter("imagesize", "600")
                search?.let { parameter("search", it) }
            }.body()
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArtistTracks(artistId: String, limit: Int = 50): Result<List<Track>> {
        return try {
            val response: JamendoTracksResponse = client.get("$baseUrl/tracks") {
                parameter("client_id", clientId)
                parameter("format", "json")
                parameter("artist_id", artistId)
                parameter("limit", limit)
                parameter("order", "popularity_total")
                parameter("include", "musicinfo")
                parameter("imagesize", "600")
            }.body()
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArtistAlbums(artistId: String): Result<List<Album>> {
        return try {
            val response: JamendoAlbumsResponse = client.get("$baseUrl/albums") {
                parameter("client_id", clientId)
                parameter("format", "json")
                parameter("artist_id", artistId)
                parameter("order", "releasedate_desc")
                parameter("imagesize", "600")
            }.body()
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRadio(
        limit: Int = 50,
        tags: String? = null
    ): Result<List<Track>> {
        return getTracks(
            limit = limit,
            order = "popularity_week",
            tags = tags
        )
    }

    fun close() {
        client.close()
    }
}
