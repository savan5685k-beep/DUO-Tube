package com.edureminder.quicktube.feature.browser

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BrowserContentFilterTest {
    @Test
    fun blocksKnownAdultDomainsAndTheirSubdomains() {
        assertTrue(duoBlockedUrl("https://pornhub.com/"))
        assertTrue(duoBlockedUrl("https://www.xvideos.com/video"))
        assertTrue(duoBlockedUrl("https://subdomain.onlyfans.com/path"))
        assertTrue(duoBlockedUrl("http://xnxx.com/"))
    }

    @Test
    fun hostMatchingDoesNotBlockLookalikeSuffixes() {
        assertFalse(duoBlockedUrl("https://notpornhub.com/"))
        assertFalse(duoBlockedUrl("https://pornhub.com.example.org/"))
        assertFalse(duoBlockedUrl("https://example.org/?site=pornhub.com"))
    }

    @Test
    fun normalizesWwwAndTrailingDot() {
        assertTrue(duoBlockedUrl("https://www.pornhub.com./"))
    }

    @Test
    fun blocksExplicitSearchTermsIncludingEncodedTerms() {
        assertTrue(duoBlockedUrl("https://search.example/?q=nsfw"))
        assertTrue(duoBlockedUrl("https://search.example/?q=adult-content"))
        assertTrue(duoBlockedUrl("https://search.example/?q=%70%6f%72%6e"))
        assertTrue(duoBlockedUrl("https://search.example/?q=%2570%256f%2572%256e"))
    }

    @Test
    fun doesNotBlockOrdinaryUrlsOrWordsAsSubstrings() {
        assertFalse(duoBlockedUrl("https://youtube.com/watch?v=abc"))
        assertFalse(duoBlockedUrl("https://instagram.com/"))
        assertFalse(duoBlockedUrl("https://example.org/?q=important"))
        assertFalse(duoBlockedUrl("https://example.org/?q=horn"))
    }
}
