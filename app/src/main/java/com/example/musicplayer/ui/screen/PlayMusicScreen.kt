package com.example.musicplayer.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R

/**
 * 音楽再生画面です.
 */
@Composable
fun PlayMusicScreen(onPlayButtonClick: () -> Unit) {
    val checkedState = remember { mutableStateOf(true) }
    val playState = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.padding(32.dp)) {
            Button(onClick = { /*TODO 戻るボタン処理*/ }) {
                Text(text = stringResource(id = R.string.bt_back))
            }
            Button(onClick = onPlayButtonClick) {
                Text(
                    text = if (playState.value) stringResource(id = R.string.bt_play_play) else stringResource(
                        id = R.string.bt_play_pause
                    )
                )
            }
            Button(onClick = { /*TODO 進むボタン処理*/ }) {
                Text(text = stringResource(id = R.string.bt_forward))
            }
        }
        Row {
            Text(modifier = Modifier.padding(12.dp), text = stringResource(id = R.string.sw_loop))
            Switch(
                checked = checkedState.value,
                onCheckedChange = { changedValue -> checkedState.value = changedValue })
        }
    }
}

/**
 * メイン画面のプレビュー表示用です.
 */
@Preview(showBackground = false)
@Composable
fun PreTopScreen() {
    PlayMusicScreen(onPlayButtonClick = TODO())
}