package com.example.musicplayer.ui.screen

import com.example.musicplayer.model.utility.SongData
import com.example.musicplayer.model.utility.SongState

/**
 * 音楽再生UiStateです.
 */
data class PlaySongUiState(
    var songs: List<SongData>? = emptyList(),
    var selectedSong: SongData? = null,
    val songState: SongState? = SongState.STOP,
    val currentSong: SongData? = null,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val isRepeatOneEnabled: Boolean = false
)
