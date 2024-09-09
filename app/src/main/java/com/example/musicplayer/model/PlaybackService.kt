package com.example.musicplayer.model

import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * MediaSessionServiceクラスを継承したServiceクラスです.
 * ▼【参考リンク】Media3 ExoPlayerを使用して基本的なメディア プレーヤー アプリを作成する
 * https://developer.android.com/media/implement/playback-app?hl=ja
 * ▼【参考リンク】MediaSessionService によるバックグラウンド再生
 * https://developer.android.com/media/media3/session/background-playback?hl=ja
 */
@AndroidEntryPoint
class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null

    //https://developer.android.com/training/dependency-injection/hilt-android?hl=ja
    // コンポーネントから依存関係を取得するには、@Inject アノテーションを使用してフィールド インジェクションを行う
    @Inject
    lateinit var player: ExoPlayer

    // onCreateライフサイクルイベントでメディアセッションを作成
    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSession.Builder(this, player)
            .setCallback(MediaSessionCallback())
            .build() //プレーヤーを初期化した後、MediaSessionを作成
    }


    //  クライアントがメディアセッションにアクセスできるようにする。
    //  リクエストを拒否する場合はnullを返す
    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? = mediaSession

    // ユーザーがタスクからアプリを閉じる際の処理
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player!!
        if (!player.playWhenReady
            || player.mediaItemCount == 0
            || player.playbackState == Player.STATE_ENDED
        ) {
            // 再生していない場合はサービスを停止し、それ以外の場合はバックグラウンドで再生を続行
            stopSelf()
        }
    }

    // プレーヤーとメディアセッションを解放する
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }

        super.onDestroy()
    }

    private inner class MediaSessionCallback : MediaSession.Callback {
        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>
        ): ListenableFuture<MutableList<MediaItem>> {
            val updatedMediaItems = mediaItems.map {
                it.buildUpon().setUri(it.mediaId).build()
            }.toMutableList()

            return Futures.immediateFuture(updatedMediaItems)
        }
    }
}