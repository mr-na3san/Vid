package com.vidsaver.app.ui.main

import com.vidsaver.app.data.model.VideoResult

data class MainUiState(
    val urlInput: String = "",
    val isLoading: Boolean = false,
    val isDownloading: Boolean = false,
    val video: VideoResult? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val downloadedFilePath: String? = null
)
