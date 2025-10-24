package org.roshan.beats.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.roshan.beats.data.models.Track

data class PlayerState(
    val currentTrack: Track? = null,
    val queue: List<Track> = emptyList(),
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val duration: Long = 0,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val audioQuality: AudioQuality = AudioQuality.HIGH,
    val isAiModeEnabled: Boolean = false,
    val crossfadeDuration: Int = 3
)

enum class RepeatMode {
    OFF, ONE, ALL
}

enum class AudioQuality {
    LOW, MEDIUM, HIGH, LOSSLESS
}

class PlayerViewModel : ViewModel() {
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()
    
    fun playTrack(track: Track) {
        _playerState.update {
            it.copy(
                currentTrack = track,
                isPlaying = true,
                currentPosition = 0
            )
        }
    }
    
    fun playQueue(tracks: List<Track>, startIndex: Int = 0) {
        if (tracks.isEmpty()) return
        
        _playerState.update {
            it.copy(
                queue = tracks,
                currentTrack = tracks[startIndex],
                isPlaying = true,
                currentPosition = 0
            )
        }
    }
    
    fun togglePlayPause() {
        _playerState.update {
            it.copy(isPlaying = !it.isPlaying)
        }
    }
    
    fun playNext() {
        val currentState = _playerState.value
        val currentIndex = currentState.queue.indexOf(currentState.currentTrack)
        
        if (currentIndex < currentState.queue.size - 1) {
            _playerState.update {
                it.copy(
                    currentTrack = it.queue[currentIndex + 1],
                    currentPosition = 0
                )
            }
        } else if (currentState.repeatMode == RepeatMode.ALL && currentState.queue.isNotEmpty()) {
            _playerState.update {
                it.copy(
                    currentTrack = it.queue[0],
                    currentPosition = 0
                )
            }
        }
    }
    
    fun playPrevious() {
        val currentState = _playerState.value
        
        // If more than 3 seconds into the song, restart it
        if (currentState.currentPosition > 3000) {
            _playerState.update { it.copy(currentPosition = 0) }
            return
        }
        
        val currentIndex = currentState.queue.indexOf(currentState.currentTrack)
        if (currentIndex > 0) {
            _playerState.update {
                it.copy(
                    currentTrack = it.queue[currentIndex - 1],
                    currentPosition = 0
                )
            }
        } else if (currentState.repeatMode == RepeatMode.ALL && currentState.queue.isNotEmpty()) {
            _playerState.update {
                it.copy(
                    currentTrack = it.queue[it.queue.size - 1],
                    currentPosition = 0
                )
            }
        }
    }
    
    fun seekTo(position: Long) {
        _playerState.update { it.copy(currentPosition = position) }
    }
    
    fun toggleShuffle() {
        _playerState.update {
            val newQueue = if (!it.isShuffleEnabled) {
                it.queue.shuffled()
            } else {
                // Restore original order (in real app, you'd store the original)
                it.queue.sortedBy { track -> track.name }
            }
            it.copy(
                isShuffleEnabled = !it.isShuffleEnabled,
                queue = newQueue
            )
        }
    }
    
    fun cycleRepeatMode() {
        _playerState.update {
            val newMode = when (it.repeatMode) {
                RepeatMode.OFF -> RepeatMode.ALL
                RepeatMode.ALL -> RepeatMode.ONE
                RepeatMode.ONE -> RepeatMode.OFF
            }
            it.copy(repeatMode = newMode)
        }
    }
    
    fun toggleAiMode() {
        _playerState.update {
            it.copy(isAiModeEnabled = !it.isAiModeEnabled)
        }
    }
    
    fun setAudioQuality(quality: AudioQuality) {
        _playerState.update { it.copy(audioQuality = quality) }
    }
    
    fun setCrossfadeDuration(seconds: Int) {
        _playerState.update { it.copy(crossfadeDuration = seconds) }
    }
    
    fun addToQueue(track: Track) {
        _playerState.update {
            it.copy(queue = it.queue + track)
        }
    }
    
    fun removeFromQueue(index: Int) {
        _playerState.update {
            val newQueue = it.queue.toMutableList()
            newQueue.removeAt(index)
            it.copy(queue = newQueue)
        }
    }
    
    fun clearQueue() {
        _playerState.update {
            it.copy(
                queue = listOfNotNull(it.currentTrack),
                currentPosition = 0
            )
        }
    }
    
    fun updateProgress(position: Long, duration: Long) {
        _playerState.update {
            it.copy(
                currentPosition = position,
                duration = duration
            )
        }
    }
}
