package com.example.musicplayer.model

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicplayer.model.utility.SongData
import com.example.musicplayer.model.utility.SongState
import com.example.musicplayer.model.utility.toSong
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

/**
 * MediaSessionとやり取りしてメディアのクエリと制御を行うためのコントローラーです.
 * ▼【参考リンク】メディアアプリに接続する
 * https://developer.android.google.cn/media/media3/session/connect-to-media-app?hl=ja
 */
class MusicControllerImpl(context: Context) : MusicController {

    private var controllerFuture: ListenableFuture<MediaController>
    private val mediaController: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null

    override var mediaControllerCallback: (
        (
        songState: SongState,
        currentMusic: SongData?,
        currentPosition: Long,
        totalDuration: Long,
        isRepeatOneEnabled: Boolean
    ) -> Unit
    )? = null

    init {
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({ controllerListener() }, MoreExecutors.directExecutor())
    }


    private fun controllerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)

                with(player) {
                    mediaControllerCallback?.invoke(
                        playbackState.toSongState(isPlaying),//公式ドキュメントPlayer プレーヤーの状態 1.再生状態
                        currentMediaItem?.toSong(),//公式ドキュメントPlayer プレーヤーの状態 2.再生状態 メディア アイテムの再生リスト
                        currentPosition.coerceAtLeast(0L),//公式ドキュメントPlayer プレーヤーの状態 4.再生位置 再生位置をミリ秒単位で返す、0より小さくないか確認しながら
                        duration.coerceAtLeast(0L),//継続時間をミリ秒単位で返す、何秒進んだか表示するため。
                        repeatMode == Player.REPEAT_MODE_ONE //リピートモードが1(1曲リピート)ならtrue
                    )
                }
            }
        })
    }

    //Playerインターフェースで定義された現在の再生状態→独自実装の状態に変換
    private fun Int.toSongState(isPlaying: Boolean) =
        when (this) {
            Player.STATE_IDLE -> SongState.STOP
            Player.STATE_ENDED -> SongState.STOP
            else -> if (isPlaying) SongState.PLAYING else SongState.PAUSE
        }

    //https://developer.android.com/media/media3/exoplayer/playlists?hl=ja
    override fun initializePlaylist(songs: List<SongData>) {
        val mediaItems = songs.map {
            MediaItem.Builder()
                .setMediaId(it.songUrl)
                .setUri(it.songUrl)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(it.title)
                        .setSubtitle(it.subtitle)
                        .setArtist(it.subtitle)
                        .setArtworkUri(Uri.parse(it.imageUrl))
                        .build()
                )
                .build()
        }

        mediaController?.setMediaItems(mediaItems)
    }

    override fun play(mediaItemIndex: Int) {
        mediaController?.seekToDefaultPosition(mediaItemIndex)
        mediaController?.prepare()
    }

    override fun resume() {
        mediaController?.play()
    }

    override fun pause() {
        mediaController?.pause()
    }

    override fun getCurrentPosition(): Long = mediaController?.currentPosition ?: 0L

    override fun getCurrentSong(): SongData? = mediaController?.currentMediaItem?.toSong()

    override fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }

    override fun changeRepeatMode(repeatMode: Int) {
        mediaController?.repeatMode = repeatMode
    }

    override fun skipNext() {
        mediaController?.seekToNext()
    }

    override fun skipPrevious() {
        mediaController?.seekToPrevious()
    }

    override fun destroy() {
        MediaController.releaseFuture(controllerFuture)
        mediaControllerCallback = null
    }
}