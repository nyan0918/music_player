package com.example.musicplayer

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import com.example.musicplayer.ui.screen.PlayMusicScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * メインアクティビティです.
 */
class MainActivity : ComponentActivity() {

    private var _player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlayMusicScreen(onPlayButtonClick = { onPlayButtonClick() })
        }

        _player = MediaPlayer()

        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.bgm}"

        val mediaFileUri = Uri.parse(mediaFileUriStr)

        _player?.let {
            it.setDataSource(this@MainActivity, mediaFileUri)
//            it.setOnPreparedListener(PlayerPreparedListener())
            it.prepareAsync()
        }
    }

    override fun onStop() {
        _player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        super.onStop()
    }

//    private inner class PlayerPreparedListener : MediaPlayer.OnPreparedListener {
//        override fun onPrepared(mp: MediaPlayer) {
//            //TODO あとでかく
//        }
//    }

    /**
     * 再生ボタン押下時の処理です.
     */
    private fun onPlayButtonClick() {
        _player?.let {
            if (it.isPlaying) {
                it.pause()

            } else {
                it.start()
            }
        }
    }
}
