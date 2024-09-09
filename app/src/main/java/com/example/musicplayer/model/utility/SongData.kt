package com.example.musicplayer.model.utility

import androidx.media3.common.MediaItem
import com.example.musicplayer.R

/**
 * 楽曲情報データクラスです.
 */
data class SongData(
    val mediaId: String,
    val title: String,
    val subtitle: String,
    val songUrl: String,
    val imageUrl: String
)

/**
 * 取得したMediaItemに入ってる曲の各情報を
 * アプリ側で使用する用のString型に変換して独自実装の楽曲情報データクラスにまとめる.
 */
fun MediaItem.toSong() =
    SongData(
        mediaId = mediaId,
        title = mediaMetadata.title.toString(),
        subtitle = mediaMetadata.subtitle.toString(),
        songUrl = mediaId,
        imageUrl = mediaMetadata.artworkUri.toString()
    )

/**
 * 楽曲情報データクラスをリスト化してプレイリスト情報として渡します.
 */
fun getDefaultPlayList() = listOf(
    SongData(
        mediaId = "song-1",
        title = "Song1",
        subtitle = "Singer1",
        songUrl = "android.resource://com.example.musicplayer/${R.raw.bgm}",
        imageUrl = "android.resource://com.example.musicplayer/${R.drawable.bgm}"
    ),
    SongData(
        mediaId = "song-2",
        title = "Song2",
        subtitle = "Singer2",
        songUrl = "android.resource://com.example.musicplayer/${R.raw.morning}",
        imageUrl = "android.resource://com.example.musicplayer/${R.drawable.morning_img}"
    ),
    SongData(
        mediaId = "song-3",
        title = "Singer3",
        subtitle = "Singer3",
        songUrl = "android.resource://com.example.musicplayer/${R.raw.am}",
        imageUrl = "android.resource://com.example.musicplayer/${R.drawable.am}"
    ),
    SongData(
        mediaId = "song-4",
        title = "Song4",
        subtitle = "Singer4",
        songUrl = "android.resource://com.example.musicplayer/${R.raw.kaeru_no_piano}",
        imageUrl = "android.resource://com.example.musicplayer/${R.drawable.kaeru_no_piano}"
    )

)
