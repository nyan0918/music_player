package com.example.musicplayer.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.musicplayer.R
import com.example.musicplayer.model.utility.SongState
import com.example.musicplayer.ui.theme.Pink40
import com.example.musicplayer.ui.theme.Purple80
import com.example.musicplayer.ui.theme.PurpleGrey80
import java.util.Locale
import kotlin.time.Duration

/**
 * 音楽再生画面です.
 */
@Composable
fun PlaySongScreen(
    viewModel: PlaySongViewModel
) {
    val musicControllerUiState = viewModel.playSongUiState

    val songState = musicControllerUiState.songState
    val currentSong = musicControllerUiState.currentSong
    val currentPosition = musicControllerUiState.currentPosition
    val totalDuration = musicControllerUiState.totalDuration
    val isRepeatOneEnabled = musicControllerUiState.isRepeatOneEnabled

    Column(
        //グラデーション背景
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Purple80, PurpleGrey80, Pink40)
                )
            )
            .padding(horizontal = 10.dp)
    ) {
        TopAppBar()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                //楽曲画像
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(currentSong?.imageUrl)
                        .crossfade(true).build()
                ),
                contentDescription = "Songs Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .sizeIn(maxWidth = 500.dp, maxHeight = 500.dp)
                    .aspectRatio(1f)
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
                    .weight(10f)
            )
            Spacer(modifier = Modifier.height(30.dp))
            currentSong?.let { SongDescription(title = it.title, name = it.subtitle) }
            Spacer(modifier = Modifier.height(35.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(10f)
            ) {
                PlayerSlider(
                    ofHours = Duration.ZERO,
                    currentPosition = currentPosition,
                    totalDuration = totalDuration,
                    onChangeSlider = { position -> viewModel.seekTo(position.toLong()) })
                Spacer(modifier = Modifier.height(40.dp))
                PlayerButton(
                    onBackButtonClick = { viewModel.skipPrevious() },
                    onReplay10ButtonClick = { viewModel.seekTo(if (currentPosition - 10 * 1000 < 0) 0 else currentPosition - 10 * 1000) },
                    onPlayButtonClick = { if (songState == SongState.PLAYING) viewModel.pause() else viewModel.resume() },
                    onForward10ButtonClick = { viewModel.seekTo(currentPosition + 10 * 1000) },
                    onSkipButtonClick = { viewModel.skipNext() },
                    onPrepareButtonClick = {
                        viewModel.initializePlaylist()
                        viewModel.prepare()
                    },
                    onChangeRepeatMode = {
                        if (isRepeatOneEnabled) viewModel.changeRepeatMode(0) else viewModel.changeRepeatMode(
                            1
                        )
                    },
                    repeatButtonColor = if (isRepeatOneEnabled) Color.Green else Color.White,
                    playButtonVector = if (songState == SongState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * 画面上部のアプリバーです.
 */
@Composable
fun TopAppBar() {
    Row(modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back Button Fake",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu Button Fake",
                tint = Color.White
            )
        }
    }
}

/**
 * 楽曲情報のタイトルと説明文です.
 */
@Composable
fun SongDescription(title: String, name: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        maxLines = 1,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )
    CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = 0.4f)) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 再生位置を変更するスライダーです.
 */
@Composable
fun PlayerSlider(
    ofHours: Duration?,
    currentPosition: Long,
    totalDuration: Long,
    onChangeSlider: (Float) -> Unit
) {
    if (ofHours != null) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = currentPosition.toFloat(),
                valueRange = 0f..totalDuration.toFloat(),
                onValueChange = onChangeSlider,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White
                )
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = toTime(currentPosition), color = Color.White)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = toTime(totalDuration))
            }
        }
    }
}

/**
 * ボタン各種です.
 */
@Composable
fun PlayerButton(
    onBackButtonClick: () -> Unit,
    onReplay10ButtonClick: () -> Unit,
    onPlayButtonClick: () -> Unit,
    onForward10ButtonClick: () -> Unit,
    onSkipButtonClick: () -> Unit,
    onPrepareButtonClick: () -> Unit,
    onChangeRepeatMode: () -> Unit,
    repeatButtonColor: Color,
    playButtonVector: ImageVector
) {
    Column {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            val buttonModifier = Modifier
                .size(72.dp)
                .semantics { role = Role.Button }
            IconButton(onClick = { onBackButtonClick() }, modifier = buttonModifier) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Skip Previous",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
            IconButton(onClick = { onReplay10ButtonClick() }, modifier = buttonModifier) {
                Icon(
                    Icons.Default.Replay10,
                    contentDescription = "Replay",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
            IconButton(onClick = { onPlayButtonClick() }, modifier = buttonModifier) {
                Icon(
                    playButtonVector,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
            IconButton(onClick = { onForward10ButtonClick() }, modifier = buttonModifier) {
                Icon(
                    Icons.Default.Forward10,
                    contentDescription = "Forward",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
            IconButton(onClick = { onSkipButtonClick() }, modifier = buttonModifier) {
                Icon(
                    Icons.Default.SkipNext,
                    contentDescription = "Skip Next",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Row {
            IconButton(onClick = { onPrepareButtonClick() }) {
                Icon(
                    Icons.Default.AddCircleOutline,
                    contentDescription = "Prepare",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
            IconButton(onClick = { onChangeRepeatMode() }) {
                Icon(
                    Icons.Default.Repeat,
                    contentDescription = "Repeat Mode",
                    tint = repeatButtonColor,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

private fun toTime(time: Long): String {
    val stringBuffer = StringBuffer()

    val min = (time / 60000).toInt()
    val sec = (time % 60000 / 1000).toInt()

    stringBuffer
        .append(String.format(Locale.JAPAN, "%2d", min))
        .append(":")
        .append(String.format(Locale.JAPAN, "%02d", sec))

    return stringBuffer.toString()
}

@Preview(showBackground = false)
@Composable
fun PreTopAppBar() {
    Row(modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back Button",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add List",
                tint = Color.White
            )
        }
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More Vert",
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PreSongDescription() {
    Text(
        text = "title",
        style = MaterialTheme.typography.titleSmall,
        maxLines = 1,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )
    CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = 0.4f)) {
        Text(
            text = "name",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = false)
@Composable
fun PrePlayerSlider() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Slider(
            value = 0f,
            onValueChange = { TODO() },
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White
            )
        )
    }
}

@Preview(showBackground = false)
@Composable
fun PrePlayerButton(
    sideButtonSize: Dp = 72.dp
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        val buttonModifier = Modifier
            .size(sideButtonSize)
            .semantics { role = Role.Button }
        IconButton(onClick = { }, modifier = buttonModifier) {
            Icon(
                Icons.Default.SkipNext,
                contentDescription = "Skip Previous",
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
            )
        }
        IconButton(onClick = { }, modifier = buttonModifier) {
            Icon(
                Icons.Default.Replay10,
                contentDescription = "Replay",
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
            )
        }
        IconButton(onClick = { }, modifier = buttonModifier) {
            Icon(
                Icons.Default.PlayCircle,
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
            )
        }
        IconButton(onClick = { }, modifier = buttonModifier) {
            Icon(
                Icons.Default.Forward10,
                contentDescription = "Forward",
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
            )
        }
        IconButton(onClick = { }, modifier = buttonModifier) {
            Icon(
                Icons.Default.SkipNext,
                contentDescription = "Skip Next",
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PrePlayScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Purple80, PurpleGrey80, Pink40)
                )
            )
            .padding(horizontal = 10.dp)
    ) {
    }
}

@Preview(showBackground = false)
@Composable
fun PrePlayAll() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Purple80, PurpleGrey80, Pink40)
                )
            )
            .padding(horizontal = 10.dp)
    ) {
        PreTopAppBar()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Songs Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .sizeIn(maxWidth = 500.dp, maxHeight = 500.dp)
                    .aspectRatio(1f)
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
                    .weight(10f)
            )
            Spacer(modifier = Modifier.height(30.dp))
            SongDescription(title = "Title", name = "Singer")
            Spacer(modifier = Modifier.height(35.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(10f)
            ) {
                PlayerSlider(
                    ofHours = Duration.ZERO,
                    currentPosition = 0,
                    totalDuration = 0,
                    onChangeSlider = {})
                Spacer(modifier = Modifier.height(40.dp))
                PrePlayerButton()
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
