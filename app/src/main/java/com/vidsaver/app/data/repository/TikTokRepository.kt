package com.vidsaver.app.data.repository

import com.vidsaver.app.data.model.VideoResult

sealed class ExtractionResult {
    data class Success(val video: VideoResult) : ExtractionResult()
    data class Error(val message: String) : ExtractionResult()
}

sealed class DownloadResult {
    data class Success(val filePath: String) : DownloadResult()
    data class Error(val message: String) : DownloadResult()
}

interface TikTokRepository {
    suspend fun extractVideo(url: String): ExtractionResult
    suspend fun downloadVideo(videoUrl: String, fileName: String): DownloadResult
}
