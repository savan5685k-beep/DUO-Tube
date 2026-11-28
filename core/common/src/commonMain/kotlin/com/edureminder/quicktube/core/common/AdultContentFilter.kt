package com.edureminder.quicktube.core.common

import com.edureminder.quicktube.core.model.MediaItem

/**
 * Conservative URL/search keyword guard. It catches explicit terms and known adult domains before routing or requests.
 * This is not a media classifier: images and videos hosted on allowed platforms may still contain adult material.
 */
object AdultContentFilter {
    private val explicitTerms = Regex(
        """(?i)(?<![a-z0-9])(?:porn(?:ography|ographic)?|porno|xxx|sex|sexcam|webcamsex|hentai|rule34|nsfw|adult(?:[-_ ]?content)?|erotic|explicit(?:[-_ ]?content)?|sexual(?:[-_ ]?content)?|nude|nudity|fetish|masturbation)(?![a-z0-9])"""
    )
    private val adultSiteNames = Regex(
        """(?i)(?<![a-z0-9])(?:pornhub|xvideos[0-9]*|xnxx[0-9]*|xhamster|redtube|youporn|tube8|spankbang|spankwire|eporner|tnaflix|beeg|porntrex|chaturbate|stripchat|livejasmin|cam4|myfreecams|onlyfans|fansly|manyvids|nhentai|hentaihaven|hanime|f95zone|literotica|sexcom|adultfriendfinder|ashleymadison|brazzers|bangbros|mofos|naughtyamerica|realitykings|digitalplayground|javhd|javlibrary|avgle|sxyprn|motherless|heavyr|ixxx|porndig|porn300|porn555|pornhd|pornhits|pornovideos|sexvid|sexvideos|sexstories|hentaifox|hentai2read|hentaiworld|hentaitube|yavtube|javguru|javmost|javbus|javbooks|adultdvdempire|adulttime|xart|fapello|redgifs)(?![a-z0-9])"""
    )
    private val blockedHosts = setOf(
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

    const val BLOCKED_MESSAGE = "DUO Safe Browsing blocked this search or link. Try a different one."

    /** Explicit keyword check with bounded percent-decoding for URL-encoded and double-encoded text. */
    fun isBlockedText(text: String): Boolean = containsDecoded(explicitTerms, text)

    /** Search-input check also rejects known adult-site names; URL checks use the stricter host parser instead. */
    fun isBlockedSearchText(text: String): Boolean = isBlockedText(text) || containsDecoded(adultSiteNames, text)

    /** Matches normalized ASCII hosts and their subdomains, not lookalike suffixes. */
    fun isBlockedHost(host: String): Boolean {
        val normalized = host.trim().trimEnd('.').removePrefix("www.").lowercase()
        return normalized.isNotEmpty() && blockedHosts.any { normalized == it || normalized.endsWith(".$it") }
    }

    /** Checks explicit URL terms and the actual HTTP(S) hostname; query strings containing a domain name are not hosts. */
    fun isBlockedUrlOrText(raw: String): Boolean {
        if (isBlockedText(raw)) return true
        val host = extractHost(raw) ?: return false
        return isBlockedHost(host)
    }

    /** Filters search-result metadata only; it does not inspect thumbnails or video frames. */
    fun filterMediaItems(items: List<MediaItem>): List<MediaItem> =
        items.filterNot { isBlockedSearchText(it.title) || isBlockedSearchText(it.uploader) || isBlockedUrlOrText(it.url) }

    private fun extractHost(raw: String): String? {
        val scheme = raw.substringBefore("://", "").lowercase()
        if (scheme != "http" && scheme != "https") return null
        val authority = raw.substringAfter("://").substringBefore('/').substringBefore('?').substringBefore('#').substringAfterLast('@')
        if (authority.startsWith("[")) return null // IPv6 literals aren't adult-domain matches.
        return authority.substringBefore(':').trimEnd('.').lowercase().takeIf { it.isNotBlank() }
    }

    private fun containsDecoded(regex: Regex, text: String): Boolean {
        var candidate = text
        repeat(3) {
            if (regex.containsMatchIn(candidate)) return true
            val decoded = decodePercentAscii(candidate)
            if (decoded == candidate) return false
            candidate = decoded
        }
        return regex.containsMatchIn(candidate)
    }

    private fun decodePercentAscii(text: String): String =
        Regex("%([0-9a-fA-F]{2})").replace(text) { match ->
            match.groupValues[1].toInt(16).toChar().toString()
        }.replace('+', ' ')
}
