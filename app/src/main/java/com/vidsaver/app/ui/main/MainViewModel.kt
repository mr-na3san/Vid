package com.vidsaver.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidsaver.app.data.repository.DownloadResult
import com.vidsaver.app.data.repository.ExtractionResult
import com.vidsaver.app.data.repository.TikTokRepository
import com.vidsaver.app.domain.UrlValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(private val repository: TikTokRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    fun onUrlChanged(newValue: String) {
        _uiState.update { it.copy(urlInput = newValue, errorMessage = null) }
    }

    fun onSharedTextReceived(sharedText: String) {
        val extracted = UrlValidator.extractFirstUrl(sharedText) ?: sharedText.trim()
        _uiState.update { it.copy(urlInput = extracted) }
        fetchVideo()
    }

    fun fetchVideo() {
        val url = _uiState.value.urlInput.trim()
        if (url.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please enter a TikTok link first.") }
            return
        }
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
                video = null,
                downloadedFilePath = null
            )
        }
        viewModelScope.launch {
            when (val result = repository.extractVideo(url)) {
                is ExtractionResult.Success -> _uiState.update {
                    it.copy(isLoading = false, video = result.video, successMessage = "Video ready to download.")
                }
                is ExtractionResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }

    fun downloadVideo() {
        val video = _uiState.value.video ?: return
        _uiState.update { it.copy(isDownloading = true, errorMessage = null, successMessage = null) }
        viewModelScope.launch {
            val fileName = "vidsaver_${UUID.randomUUID().toString().take(8)}.mp4"
            when (val result = repository.downloadVideo(video.downloadUrl, fileName)) {
                is DownloadResult.Success -> _uiState.update {
                    it.copy(
                        isDownloading = false,
                        downloadedFilePath = result.filePath,
                        successMessage = "Video saved successfully."
                    )
                }
                is DownloadResult.Error -> _uiState.update {
                    it.copy(isDownloading = false, errorMessage = result.message)
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
