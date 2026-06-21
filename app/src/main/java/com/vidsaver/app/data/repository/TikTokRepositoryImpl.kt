package com.vidsaver.app.data.repository

import android.content.Context
import com.vidsaver.app.data.model.VideoResult
import com.vidsaver.app.data.network.TikTokApiService
import com.vidsaver.app.domain.UrlValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class TikTokRepositoryImpl(
    private val api: TikTokApiService,
    private val httpClient: OkHttpClient,
    private val appContext: Context
) : TikTokRepository {

    override suspend fun extractVideo(url: String): ExtractionResult = withContext(Dispatchers.IO) {
        if (!UrlValidator.isValidTikTokUrl(url)) {
            return@withContext ExtractionResult.Error("This doesn't look like a valid TikTok link.")
        }
        try {
            val response = api.fetchVideoInfo(url)
            val data = response.data
            if (response.code != 0 || data == null || data.playUrl.isNullOrBlank()) {
                val message = response.message.ifBlank { "This video is unavailable or unsupported." }
                return@withContext ExtractionResult.Error(message)
            }
            val result = VideoResult(
                title = data.title?.takeIf { it.isNotBlank() } ?: "TikTok Video",
                authorName = data.author?.nickname,
                thumbnailUrl = data.cover,
                downloadUrl = data.playUrl,
                durationSeconds = data.duration
            )
            ExtractionResult.Success(result)
        } catch (e: SocketTimeoutException) {
            ExtractionResult.Error("The request timed out. Please check your connection and try again.")
        } catch (e: UnknownHostException) {
            ExtractionResult.Error("No internet connection available.")
        } catch (e: HttpException) {
            ExtractionResult.Error("Server returned an error (${e.code()}). Please try again later.")
        } catch (e: IOException) {
            ExtractionResult.Error("Network error occurred. Please try again.")
        } catch (e: Exception) {
            ExtractionResult.Error("Something went wrong while processing this video.")
        }
    }

    override suspend fun downloadVideo(videoUrl: String, fileName: String): DownloadResult =
        withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(videoUrl).build()
                httpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        return@withContext DownloadResult.Error("Download failed (${response.code}).")
                    }
                    val body = response.body
                        ?: return@withContext DownloadResult.Error("Empty response from server.")
                    val downloadsDir = File(appContext.getExternalFilesDir(null), "Downloads")
                    if (!downloadsDir.exists()) downloadsDir.mkdirs()
                    val outputFile = File(downloadsDir, fileName)
                    FileOutputStream(outputFile).use { output ->
                        body.byteStream().use { input -> input.copyTo(output) }
                    }
                    DownloadResult.Success(outputFile.absolutePath)
                }
            } catch (e: SocketTimeoutException) {
                DownloadResult.Error("Download timed out. Please try again.")
            } catch (e: IOException) {
                DownloadResult.Error("Failed to save the video file.")
            } catch (e: Exception) {
                DownloadResult.Error("Unexpected error while downloading.")
            }
        }
}
