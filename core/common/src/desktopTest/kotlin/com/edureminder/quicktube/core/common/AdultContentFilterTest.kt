package com.edureminder.quicktube.core.common

import com.edureminder.quicktube.core.model.MediaItem
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AdultContentFilterTest {
    @Test
    fun blocksExplicitTermsAndKnownAdultSiteNamesInSearches() {
        listOf("porn", "pornography", "sex", "nsfw", "hentai").forEach {
            assertTrue("Expected explicit term: $it", AdultContentFilter.isBlockedText(it))
        }
        listOf("xvideos", "xnxx", "onlyfans", "fansly").forEach {
            assertTrue("Expected blocked site name: $it", AdultContentFilter.isBlockedSearchText(it))
        }
    }

    @Test
    fun blocksAdultHostsButNotLookalikeUrlSuffixesOrSiteNamesInQueries() {
        assertTrue(AdultContentFilter.isBlockedUrlOrText("https://sub.onlyfans.com/path"))
        assertFalse(AdultContentFilter.isBlockedUrlOrText("https://notpornhub.com/"))
        assertFalse(AdultContentFilter.isBlockedUrlOrText("https://pornhub.com.example.org/"))
        assertFalse(AdultContentFilter.isBlockedUrlOrText("https://example.org/?site=pornhub.com"))
        assertTrue(AdultContentFilter.isBlockedUrlOrText("https://example.org/?q=sex"))
    }

    @Test
    fun blocksPercentEncodedAndDoubleEncodedTerms() {
        assertTrue(AdultContentFilter.isBlockedText("%70%6f%72%6e"))
        assertTrue(AdultContentFilter.isBlockedText("%2570%256f%2572%256e"))
    }

    @Test
    fun filtersSearchResultsByTitleUploaderAndUrlTerms() {
        val safe = MediaItem(url = "https://youtube.com/watch?v=safe", title = "Science", uploader = "Channel")
        val explicitTitle = MediaItem(url = "https://youtube.com/watch?v=1", title = "Porn topic")
        val explicitUploader = MediaItem(url = "https://youtube.com/watch?v=2", title = "Video", uploader = "Hentai Channel")
        val explicitUrl = MediaItem(url = "https://xvideos.com/video", title = "Video", uploader = "Channel")
        assertEquals(listOf(safe), AdultContentFilter.filterMediaItems(listOf(safe, explicitTitle, explicitUploader, explicitUrl)))
    }

    @Test
    fun doesNotBlockOrdinaryWordsContainingTermSubstrings() {
        listOf("sexton", "sexiness", "important", "saxophone", "adulting").forEach {
            assertFalse("Unexpectedly blocked: $it", AdultContentFilter.isBlockedText(it))
        }
    }
}
