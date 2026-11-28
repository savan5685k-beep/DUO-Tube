package com.edureminder.quicktube.core.common

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdultContentFilterTest {
    @Test
    fun blocksExplicitTermsAndKnownAdultSiteNames() {
        listOf("porn", "pornography", "sex", "nsfw", "hentai", "xvideos", "xnxx", "onlyfans", "fansly").forEach {
            assertTrue("Expected blocked term: $it", AdultContentFilter.isBlockedText(it))
        }
    }

    @Test
    fun blocksPercentEncodedAndDoubleEncodedTerms() {
        assertTrue(AdultContentFilter.isBlockedText("%70%6f%72%6e"))
        assertTrue(AdultContentFilter.isBlockedText("%2570%256f%2572%256e"))
    }

    @Test
    fun doesNotBlockOrdinaryWordsContainingTermSubstrings() {
        listOf("sexton", "sexiness", "important", "saxophone", "adulting").forEach {
            assertFalse("Unexpectedly blocked: $it", AdultContentFilter.isBlockedText(it))
        }
    }
}
