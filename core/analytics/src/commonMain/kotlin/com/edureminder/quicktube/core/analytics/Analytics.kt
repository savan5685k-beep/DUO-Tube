package com.edureminder.quicktube.core.analytics

/**
 * Analytics hooks are compatibility-only; this privacy fork sends no telemetry.
 */
interface Analytics {
    fun screen(name: String)
    fun event(name: String, params: Map<String, Any?> = emptyMap())
    /** Records a handled problem for crash reporting (no-op when reporting is off). */
    fun recordError(t: Throwable)
    /** The user's choice in Settings → Privacy. */
    fun setEnabled(enabled: Boolean)
}

/** Event names, so they stay consistent across features. */
object Events {
    const val DOWNLOAD_STARTED = "download_started"
    const val DOWNLOAD_COMPLETED = "download_completed"
    /**
     * A download that failed: [stage] "find" (the link gave no video) or "download" (saving it failed), with the site's
     * domain, platform, country and a short reason code. Never logged as a crash.
     */
    const val DOWNLOAD_FAILED = "download_failed"
    const val SHARE_TO_DOWNLOAD = "share_to_download"
    const val SEARCH = "search"
    const val THEME_CHANGED = "theme_changed"
}

/** Always-no-op telemetry implementation for the privacy-focused DUO fork. */
object NoOpAnalytics : Analytics {
    override fun screen(name: String) = Unit
    override fun event(name: String, params: Map<String, Any?>) = Unit
    override fun recordError(t: Throwable) = Unit
    override fun setEnabled(enabled: Boolean) = Unit
}

/** Platform binding: no-op on Android and desktop. */
expect val analyticsModule: org.koin.core.module.Module
