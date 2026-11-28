# DUO Tube production-readiness progress

Updated: 2026-11-28

## Current main

- SHA: a8872fc6ef0edac896414469bba33012fcc38a2d
- Current main includes the branding, voice-search, privacy, security, search-suggestion, and download-flow changes plus a test fix for DUO metadata.

## Implemented

- DUO branding, vector splash artwork, and welcome UI.
- Removed Quick Apps/Games promotional flow and unused games-fetch dependency.
- Status Saver is available through bottom navigation and opens its saver screen directly.
- About/search branding updated; quick website choices restricted to YouTube, Instagram, and Facebook.
- In-app updater/APK installation flow and install permission removed.
- Analytics/crash telemetry SDKs and unused games Remote Config fetch removed; analytics abstraction is no-op.
- Voice-search permission is requested only when the user invokes voice search. Voice results fill the query without auto-submit.
- URL/content filtering is applied in the browser, search, share/open routing, download-sheet, and bulk-download flows. WebView Safe Browsing, mixed-content blocking, and SSL-error cancellation are enabled.
- Existing content filter is retained. I will not expand or publish explicit domain/keyword lists; generic filter logic and privacy/security review can continue.

## Commits and validation

- 6cefe7c9 — WebView mixed-content and SSL hardening. CI and APK build passed (runs 46137888439 and 46137888493).
- f76141e0 — search filtering, quick-site restrictions, branding, and splash. APK build step succeeded but tests failed; the false-positive handling was corrected later.
- e396563f — result metadata filtering. Build failed due a missing model import; corrected in 55a0ebce.
- 43deff58 — test import fix. Build still failed due the corresponding main-source import; corrected in 55a0ebce.
- 55a0ebce — common URL/search filter and host parsing. APK build step passed, but unit tests failed. GitHub log retrieval returned unavailable logs; a failed-job rerun is pending, so the root cause is not confirmed.
- 7da1d049 — blocking at share, download-sheet, and bulk routes. APK build step passed; unit tests were still running when newer commits cancelled CI.
- 68f7ebd5 — subscriptions/playlists UI changes; mirror succeeded, APK build was still in progress.
- 3e2c7688 — tab-transition refinement; mirror succeeded, CI/APK were in progress.
- b1f4a96b — latest main before this iteration: APK compile succeeded, but 2 desktop tests failed in `:core:download:desktopTest` (`AudioTagsTest.videoWithHeaderAtTheEndIsBrandedInPlace` and `mp3GetsTheBrandingFrames`). Log root cause: test assertions still hard-coded the legacy `edureminder.com` branding while implementation uses `Branding.URL` and `Branding.APP` for DUO.
- a8872fc6 — updated AudioTags tests to assert against the shared DUO branding constants (`Branding.APP`, `Branding.URL`), rather than obsolete domain text. Tests/build are pending on this commit; no pass is claimed.
- The latest APK artifact has not been verified or promoted as a QA download.

## Privacy and security audit

- No analytics/crash telemetry SDK is present; the games Remote Config fetch is removed.
- Microphone permission is on-demand. The Android system speech-recognition provider may process audio off-device; DUO does not store voice recordings.
- WebView Safe Browsing is enabled; mixed HTTP subresources are blocked on HTTPS pages; SSL errors are cancelled.
- Cleartext HTTP remains enabled for user-directed browser/downloader HTTP support. This is a residual risk, not a blanket recommendation.
- The exported MediaSession is intentional for external playback controls. Media3 provides read-only access to untrusted controllers by default; trusted controllers may control playback.
- JavaScript bridge interfaces remain a WebView review item because media sniffing needs page integration.
- URL filtering cannot classify every image/video hosted on otherwise allowed sites.
- Device QA is still required for permissions, Hindi/English speech-recognition availability, downloads, Status Saver, PiP, WebView behavior, and the complete UI.

Production readiness is not declared.
