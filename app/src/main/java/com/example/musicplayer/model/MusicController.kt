package com.example.musicplayer.model

import com.example.musicplayer.model.utility.SongData
import com.example.musicplayer.model.utility.SongState

/**
 * MediaSessionとやり取りしてメディアのクエリと制御を行うためのメディアコントローラーインターフェースです.
 * ▼【参考リンク】メディアアプリに接続する
 * https://developer.android.google.cn/media/media3/session/connect-to-media-app?hl=ja
 */
interface MusicController {
    var mediaControllerCallback: (
        (
        songState: SongState,
        currentMusic: SongData?,
        currentPosition: Long,
        totalDuration: Long,
        isRepeatOneEnabled: Boolean
    ) -> Unit
    )?

    fun initializePlaylist(songs: List<SongData>)

    fun play(mediaItemIndex: Int)

    fun resume()

    fun pause()

    fun getCurrentPosition(): Long

    fun getCurrentSong(): SongData?

    fun seekTo(position: Long)

    fun changeRepeatMode(repeatMode: Int)

    fun skipNext()

    fun skipPrevious()

    fun destroy()
}