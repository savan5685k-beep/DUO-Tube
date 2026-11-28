package com.edureminder.quicktube.core.common

/** DUO branding written into saved file names, descriptions, tags, and encoder metadata. */
object Branding {
    const val APP = "DUO Tube"
    const val DOMAIN = "DUO"
    const val URL = "https://github.com/savan5685k-beep/DUO-Tube"

    /**
     * Added to the end of every file name, before the extension. Letters, digits, spaces, a dash and dots only, so the
     * name stays valid on every phone, SD card (FAT32/exFAT), computer and cloud drive.
     */
    const val FILE_SUFFIX = " - $APP"

    const val DESCRIPTION = "Downloaded with $APP · $URL"
    const val ENCODER = APP

    val KEYWORDS = listOf(
        APP, DOMAIN, "video downloader", "YouTube downloader", "YouTube to MP3",
        "YouTube to MP4", "Facebook video downloader", "Instagram reels downloader", "TikTok downloader",
        "4K video downloader", "HD video downloader", "music downloader", "MP3 converter", "offline video player",
        "free video downloader", "status saver",
    )

    /** "Song (720p)" → "Song (720p) - DUO Tube"; a name that already ends with it is left alone. */
    fun fileName(base: String): String = if (base.endsWith(FILE_SUFFIX)) base else base + FILE_SUFFIX
}
