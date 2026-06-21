package com.vidsaver.app.domain

object UrlValidator {

    private val tikTokHostPattern = Regex(
        "^(https?://)?([a-z0-9-]+\\.)?tiktok\\.com/.+",
        RegexOption.IGNORE_CASE
    )

    private val urlExtractionPattern = Regex("(https?://[^\\s]+)")

    fun isValidTikTokUrl(input: String): Boolean {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return false
        return tikTokHostPattern.matches(trimmed)
    }

    fun extractFirstUrl(text: String): String? {
        return urlExtractionPattern.find(text)?.value
    }
}
