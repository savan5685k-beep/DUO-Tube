package com.edureminder.quicktube.core.common

/**
 * Conservative keyword guard for search input and browser URLs.
 * It catches explicit terms before search suggestions/results are requested.
 * It is not a media classifier: images and videos on allowed platforms may still contain adult material.
 */
object AdultContentFilter {
    private val explicitTerms = Regex(
        """(?i)(?<![a-z0-9])(?:porn(?:ography|ographic)?|porno|pornhub|xvideos|xnxx|xhamster|redtube|youporn|tube8|spankbang|spankwire|eporner|xxx|sex|sexcam|webcamsex|hentai|rule34|nsfw|adult(?:[-_ ]?content)?|erotic|explicit(?:[-_ ]?content)?|sexual(?:[-_ ]?content)?|nude|nudity|fetish|masturbation|onlyfans|fansly|chaturbate|stripchat|adultfriendfinder|ashleymadison|brazzers|bangbros|mofos|naughtyamerica|realitykings|digitalplayground|javhd|javlibrary|avgle|fapello|motherless|redgifs)(?![a-z0-9])"""
    )

    const val BLOCKED_MESSAGE = "DUO Safe Browsing blocked this search term. Try a different search."

    /** Checks raw text and up to two percent-decoding passes, including common double-encoded input. */
    fun isBlockedText(text: String): Boolean {
        var candidate = text
        repeat(3) {
            if (explicitTerms.containsMatchIn(candidate)) return true
            val decoded = decodePercentAscii(candidate)
            if (decoded == candidate) return false
            candidate = decoded
        }
        return explicitTerms.containsMatchIn(candidate)
    }

    private fun decodePercentAscii(text: String): String =
        Regex("%([0-9a-fA-F]{2})").replace(text) { match ->
            match.groupValues[1].toInt(16).toChar().toString()
        }.replace('+', ' ')
}
