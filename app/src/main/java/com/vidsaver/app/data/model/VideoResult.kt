package com.vidsaver.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TikTokApiResponse(
    @SerialName("code") val code: Int,
    @SerialName("msg") val message: String,
    @SerialName("data") val data: TikTokApiData? = null
)

@Serializable
data class TikTokApiData(
    @SerialName("id") val id: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("cover") val cover: String? = null,
    @SerialName("play") val playUrl: String? = null,
    @SerialName("wmplay") val watermarkedPlayUrl: String? = null,
    @SerialName("duration") val duration: Int? = null,
    @SerialName("author") val author: TikTokAuthor? = null
)

@Serializable
data class TikTokAuthor(
    @SerialName("nickname") val nickname: String? = null
)

data class VideoResult(
    val title: String,
    val authorName: String?,
    val thumbnailUrl: String?,
    val downloadUrl: String,
    val durationSeconds: Int?
)
