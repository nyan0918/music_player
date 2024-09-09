package com.example.musicplayer.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.model.MusicController
import com.example.musicplayer.model.utility.SongState
import com.example.musicplayer.model.utility.getDefaultPlayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 音楽再生画面ViewModelです.
 */
@HiltViewModel
class PlaySongViewModel @Inject constructor(private val musicController: MusicController) :
    ViewModel() {
    var playSongUiState by mutableStateOf(PlaySongUiState())
        private set

    init {
        setCallback()
    }

    private fun setCallback() {
        musicController.mediaControllerCallback =
            { songState, currentMusic, currentPosition, totalDuration, isRepeatOneEnabled ->
                playSongUiState = playSongUiState.copy(
                    songState = songState,
                    currentSong = currentMusic,
                    currentPosition = currentPosition,
                    totalDuration = totalDuration,
                    isRepeatOneEnabled = isRepeatOneEnabled
                )
                if (songState == SongState.PLAYING) {
                    viewModelScope.launch {
                        while (true) {
                            delay(3)
                            playSongUiState = playSongUiState.copy(
                                currentPosition = musicController.getCurrentPosition()
                            )
                        }
                    }
                }
            }
    }

    fun initializePlaylist() {
        val songs = getDefaultPlayList()
        playSongUiState = playSongUiState.copy(songs = songs, selectedSong = songs[0])
        musicController.initializePlaylist(songs)
    }

    fun prepare() {
        playSongUiState.apply {
            songs?.indexOf(selectedSong)?.let { song ->
                musicController.play(song)
            }
        }
    }

    fun resume() {
        musicController.resume()
    }

    fun pause() {
        musicController.pause()
    }

    fun skipNext() {
        musicController.skipNext()
    }

    fun skipPrevious() {
        musicController.skipPrevious()
    }

    fun seekTo(position: Long) {
        musicController.seekTo(position)
    }

    fun changeRepeatMode(repeatMode: Int) {
        musicController.changeRepeatMode(repeatMode)
    }

    fun destroy() {
        musicController.destroy()
    }
}