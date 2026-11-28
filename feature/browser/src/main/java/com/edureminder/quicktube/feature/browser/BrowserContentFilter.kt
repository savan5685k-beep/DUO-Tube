package com.edureminder.quicktube.feature.browser

import java.net.IDN
import java.net.URI
import com.edureminder.quicktube.core.common.AdultContentFilter
import android.webkit.WebResourceResponse

/**
 * Conservative URL-level adult-content filter.
 *
 * This blocks known domains and explicit URL/search terms. It is not an image/video classifier
 * or a complete parental-control system; content hosted on allowed sites can still bypass it.
 */
private fun normalizedHost(uri: URI): String? {
    var host = uri.host
    if (host.isNullOrBlank()) {
        val authority = uri.rawAuthority ?: return null
        val withoutUserInfo = authority.substringAfterLast('@')
        // URI.host is null for some non-ASCII hostnames; don't attempt to reinterpret IPv6 literals.
        if (withoutUserInfo.startsWith("[")) return null
        host = withoutUserInfo.substringBeforeLast(':', withoutUserInfo)
    }
    val cleaned = host.trim().trimEnd('.').removePrefix("www.")
    if (cleaned.isBlank()) return null
    return runCatching { IDN.toASCII(cleaned).lowercase() }.getOrNull()
}

/** Returns true for known adult domains/subdomains or explicit adult terms in a URL/search string. */
internal fun duoBlockedUrl(raw: String): Boolean {
    val uri = runCatching { URI(raw) }.getOrNull()
    val scheme = uri?.scheme?.lowercase()
    val host = uri?.let(::normalizedHost)
    val hostBlocked = (scheme == "http" || scheme == "https") && host != null && AdultContentFilter.isBlockedHost(host)

    // Decode at most twice to catch ordinary and double-encoded search terms without unbounded decoding.
    var candidate = raw
    val termBlocked = AdultContentFilter.isBlockedText(raw)
    return hostBlocked || termBlocked
}


/** Shared, cache-free block response for main browser and sign-in WebViews. */
internal fun duoBlockResponseHtml(): String = """<!doctype html><meta charset="utf-8"><meta name="viewport" content="width=device-width"><title>Blocked</title><body style="font-family:sans-serif;background:#111;color:#eee;padding:24px"><h2>DUO Safe Browsing</h2><p>This page or request was blocked because it may contain adult content.</p><p>Safe browsing protection is enabled.</p></body>"""

internal fun duoBlockResponse(): WebResourceResponse = WebResourceResponse(
    "text/html", "UTF-8", 403, "Blocked by DUO Safe Browsing",
    mapOf("Cache-Control" to "no-store"), java.io.ByteArrayInputStream(duoBlockResponseHtml().toByteArray(Charsets.UTF_8))
)
