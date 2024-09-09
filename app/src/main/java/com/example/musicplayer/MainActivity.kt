package com.example.musicplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.musicplayer.model.PlaybackService
import com.example.musicplayer.ui.screen.PlaySongScreen
import com.example.musicplayer.ui.screen.PlaySongViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * メインアクティビティです.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val playSongViewModel: PlaySongViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaySongScreen(viewModel = playSongViewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playSongViewModel.destroy()
        stopService(Intent(this, PlaybackService::class.java))
    }
}
