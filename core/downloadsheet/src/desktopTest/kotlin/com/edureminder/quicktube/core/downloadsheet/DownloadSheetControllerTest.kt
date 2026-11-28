package com.edureminder.quicktube.core.downloadsheet

import org.junit.Assert.assertEquals
import org.junit.Test

class DownloadSheetControllerTest {
    @Test
    fun adultPageLinksAreBlockedAtTheCentralDownloadController() {
        val controller = DownloadSheetController()
        controller.open("https://pornhub.com/video")
        assertEquals(SheetTarget.Blocked("https://pornhub.com/video"), controller.target.value)
    }

    @Test
    fun safePageLinksStillOpenNormally() {
        val controller = DownloadSheetController()
        controller.open("https://youtube.com/watch?v=abc")
        assertEquals(SheetTarget.Page("https://youtube.com/watch?v=abc"), controller.target.value)
    }
}
