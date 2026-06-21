package com.vidsaver.app

import com.vidsaver.app.domain.UrlValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UrlValidatorTest {

    @Test
    fun validTikTokUrlsAreAccepted() {
        assertTrue(UrlValidator.isValidTikTokUrl("https://www.tiktok.com/@user/video/123456789"))
        assertTrue(UrlValidator.isValidTikTokUrl("https://vt.tiktok.com/ZSabcdef/"))
        assertTrue(UrlValidator.isValidTikTokUrl("tiktok.com/@user/video/123456789"))
    }

    @Test
    fun invalidUrlsAreRejected() {
        assertFalse(UrlValidator.isValidTikTokUrl(""))
        assertFalse(UrlValidator.isValidTikTokUrl("not a url"))
        assertFalse(UrlValidator.isValidTikTokUrl("https://www.youtube.com/watch?v=123"))
    }

    @Test
    fun extractsUrlFromSharedText() {
        val shared = "Check this out https://www.tiktok.com/@user/video/123456789 so cool"
        assertEquals("https://www.tiktok.com/@user/video/123456789", UrlValidator.extractFirstUrl(shared))
    }
}
