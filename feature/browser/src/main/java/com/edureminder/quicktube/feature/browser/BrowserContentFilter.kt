package com.edureminder.quicktube.feature.browser

import java.net.IDN
import java.net.URI
import java.net.URLDecoder
import android.webkit.WebResourceResponse

/**
 * Conservative URL-level adult-content filter.
 *
 * This blocks known domains and explicit URL/search terms. It is not an image/video classifier
 * or a complete parental-control system; content hosted on allowed sites can still bypass it.
 */
private val DUO_BLOCKED_HOSTS = setOf(
    "pornhub.com", "xvideos.com", "xnxx.com", "xhamster.com", "redtube.com",
    "youporn.com", "tube8.com", "spankbang.com", "spankwire.com", "eporner.com",
    "tnaflix.com", "beeg.com", "porntrex.com", "porn.com", "rule34.xxx",
    "chaturbate.com", "stripchat.com", "livejasmin.com", "cam4.com", "myfreecams.com",
    "onlyfans.com", "fansly.com", "manyvids.com", "nhentai.net", "hentaihaven.xxx",
    "hanime.tv", "hentaistream.com", "f95zone.to", "literotica.com", "sex.com",
    "adultfriendfinder.com", "ashleymadison.com", "brazzers.com", "bangbros.com",
    "mofos.com", "naughtyamerica.com", "realitykings.com", "digitalplayground.com",
    "javhd.com", "javlibrary.com", "avgle.com", "sxyprn.com", "motherless.com",
    "heavy-r.com", "ixxx.com", "porndig.com", "pornhub.org", "pornhub.net",
    "xvideos2.com", "xvideos3.com", "xnxx2.com", "xnxx3.com",
    "porn300.com", "porn555.com", "pornhd.com", "pornhits.com", "pornovideos.com",
    "sexvid.com", "sexvideos.com", "sexstories.com", "hentai.xxx", "hentai.tv",
    "hentaifox.com", "hentai2read.com", "hentaiworld.tv", "hentaitube.com",
    "yavtube.com", "jav.guru", "javmost.com", "javbus.com", "javbooks.com",
    "adultdvdempire.com", "adulttime.com", "xart.com", "fapello.com", "fap.to",
    "redgifs.com", "gifs.com"
)

private val DUO_BLOCKED_TERMS = Regex(
    """(?i)(?<![a-z0-9])(?:porn(?:ography|ographic)?|porno|xxx|sex|sexcam|webcamsex|hentai|rule34|nsfw|adult(?:[-_ ]?content)?|erotic|explicit(?:[-_ ]?content)?|sexual[-_ ]?content|sex[-_ ]?content)(?![a-z0-9])"""
)

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
    val hostBlocked = (scheme == "http" || scheme == "https") && host != null &&
        DUO_BLOCKED_HOSTS.any { host == it || host.endsWith(".$it") }

    // Decode at most twice to catch ordinary and double-encoded search terms without unbounded decoding.
    var candidate = raw
    val termBlocked = (0..2).any {
        val matched = DUO_BLOCKED_TERMS.containsMatchIn(candidate)
        if (matched) return@any true
        candidate = runCatching { URLDecoder.decode(candidate, "UTF-8") }.getOrDefault(candidate)
        false
    }
    return hostBlocked || termBlocked
}


/** Shared, cache-free block response for main browser and sign-in WebViews. */
internal fun duoBlockResponseHtml(): String = """<!doctype html><meta charset="utf-8"><meta name="viewport" content="width=device-width"><title>Blocked</title><body style="font-family:sans-serif;background:#111;color:#eee;padding:24px"><h2>DUO Safe Browsing</h2><p>This page or request was blocked because it may contain adult content.</p><p>Safe browsing protection is enabled.</p></body>"""

internal fun duoBlockResponse(): WebResourceResponse = WebResourceResponse(
    "text/html", "UTF-8", 403, "Blocked by DUO Safe Browsing",
    mapOf("Cache-Control" to "no-store"), java.io.ByteArrayInputStream(duoBlockResponseHtml().toByteArray(Charsets.UTF_8))
)
