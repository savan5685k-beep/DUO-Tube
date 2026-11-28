package com.edureminder.quicktube.core.downloadsheet

import com.edureminder.quicktube.core.extractor.StreamCandidate
import com.edureminder.quicktube.core.common.AdultContentFilter
import com.edureminder.quicktube.core.model.DownloadOption
import com.edureminder.quicktube.core.model.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** What the sheet should show. */
sealed interface SheetTarget {
    /** A page URL the extractor can resolve (YouTube etc.). */
    data class Page(
        val url: String,
        val preview: MediaItem? = null,
        /** Skip the quick lookup and go straight to the page in the (invisible) browser: Instagram posts, photos too. */
        val browserFirst: Boolean = false,
    ) : SheetTarget

    /** Media found directly (browser sniffing, status saver): options are already known. */
    data class Direct(
        val title: String,
        val pageUrl: String?,
        val thumbnail: String?,
        val options: List<DownloadOption>,
        val headers: Map<String, String> = emptyMap(),
        /** The options are a post's items (photos and videos), not formats of one video: pick any number of them. */
        val items: Boolean = false,
    ) : SheetTarget

    /** A URL or media candidate was blocked by DUO Safe Browsing. */
    data class Blocked(val url: String) : SheetTarget

    /** Streams a web page loaded for the video being watched; resolved to the matching one(s) when the sheet opens. */
    data class Sniffed(
        val title: String,
        val pageUrl: String,
        val thumbnail: String?,
        val candidates: List<StreamCandidate>,
        val durationSec: Double?,
        val headers: Map<String, String>,
        /** Fetches a URL from inside the open page (its cookies/session); null outside the browser. */
        val pageFetch: (suspend (String) -> String?)? = null,
    ) : SheetTarget
}

/** App-wide trigger for the download sheet; any screen can call [open]. */
class DownloadSheetController {
    private val _target = MutableStateFlow<SheetTarget?>(null)
    val target: StateFlow<SheetTarget?> = _target

    fun open(url: String, preview: MediaItem? = null) = openDirect(SheetTarget.Page(url, preview))

    fun open(item: MediaItem) = open(item.url, item)

    fun openDirect(target: SheetTarget) {
        val safeTarget = when (target) {
            is SheetTarget.Page -> if (AdultContentFilter.isBlockedUrlOrText(target.url)) SheetTarget.Blocked(target.url) else target
            is SheetTarget.Direct -> {
                val safeOptions = target.options.filterNot { option ->
                    AdultContentFilter.isBlockedUrlOrText(option.url) ||
                        option.audioUrl?.let(AdultContentFilter::isBlockedUrlOrText) == true ||
                        option.images.any(AdultContentFilter::isBlockedUrlOrText)
                }
                if (target.pageUrl?.let(AdultContentFilter::isBlockedUrlOrText) == true ||
                    target.options.isNotEmpty() && safeOptions.isEmpty()) {
                    SheetTarget.Blocked(target.pageUrl ?: target.options.firstOrNull()?.url.orEmpty())
                } else target.copy(options = safeOptions)
            }
            is SheetTarget.Sniffed -> {
                val safeCandidates = target.candidates.filterNot { AdultContentFilter.isBlockedUrlOrText(it.url) }
                if (AdultContentFilter.isBlockedUrlOrText(target.pageUrl) ||
                    target.candidates.isNotEmpty() && safeCandidates.isEmpty()) {
                    SheetTarget.Blocked(target.pageUrl)
                } else target.copy(candidates = safeCandidates)
            }
            is SheetTarget.Blocked -> target
        }
        _target.value = safeTarget
    }

    fun dismiss() {
        _target.value = null
    }
}
