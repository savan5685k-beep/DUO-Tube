package com.edureminder.quicktube.feature.search

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QuickSearchSitesTest {
    @Test
    fun quickSearchSitesAreRestrictedToThreeRequestedPlatforms() {
        val sites = webSearches("safe query")
        assertEquals(listOf("YouTube", "Instagram", "Facebook"), sites.map { it.first })
        assertTrue(sites.all { it.second.startsWith("https://") })
        assertFalse(sites.any { "google." in it.second || "tiktok." in it.second })
    }
}
