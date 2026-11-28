# DUO Tube production-readiness progress

Updated: 2026-11-28

## Current main

- SHA: 06b61076e7636f7513aa3410cb39d1ab1c91ecb7
- Current main includes the branding, voice-search, privacy, security, search-suggestion, and download-flow changes plus a test fix for DUO metadata.

## Implemented

- DUO branding, vector splash artwork, and welcome UI.
- Removed Quick Apps/Games promotional flow and unused games-fetch dependency.
- Status Saver is available through bottom navigation and opens its saver screen directly.
- About/search branding updated; quick website choices restricted to YouTube, Instagram, and Facebook.
- In-app updater/APK installation flow and install permission removed.
- Analytics/crash telemetry SDKs and unused games Remote Config fetch removed; analytics abstraction is no-op.
- Voice-search permission is requested only when the user invokes voice search. Voice results fill the query without auto-submit.
- Android voice search now offers Auto, Hindi (`hi-IN`), and English (`en-IN`) modes. Auto allows Android 14+ service language detection and otherwise uses the device/recognizer language; explicit Hindi/English modes pass language and preference extras. This change is awaiting CI/device verification.
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
- a8872fc6 / 79fff8b9 — corrected AudioTags branding expectations to use the shared DUO constants instead of legacy domain text.
- d57cc6cf — fixed the remaining CI compilation error by importing shared `Branding` in the desktop test file.
- CI run 46140412771 on d57cc6cf: **passed**. Debug APK build and unit tests both succeeded; test-report upload was skipped because no failure report was needed.
- APK workflow 46140412687 on the same SHA: **passed**. Build, unit tests, and APK artifact upload succeeded.
- Artifact `DUO-debug-apk` (ID 15261108167) was downloaded and verified. ZIP test passed; it contains universal, arm64-v8a, and armeabi-v7a debug APKs. Artifact SHA-256: `9c36d0fa204b2812cb1cb0716ca3fca563e5921eb6d99f2ba7b0acfb56346565`. Universal APK ZIP integrity test passed; SHA-256: `e5f264cdf93818738b9239ff469351235218760bbd2786153b0f8e90b00bd74e`. This is a debug QA build, not a release-signed production APK.

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


## Static audit notes (2026-11-28)

- App manifest review: `INTERNET` is the app-level permission; modules add network-state, foreground download/media services, notification, wake-lock, and local media/storage permissions. Mic permission is declared in search and is requested only from the voice-search control after user invocation.
- Exported components: launcher/link entry and share receiver are exported by design; FileProvider is not exported and grants URI access; download/job services are not exported. MediaSession service is exported intentionally for external media controls and remains device QA/security review scope.
- No Firebase/Crashlytics/analytics SDK entries found in the version catalog; the analytics module is no-op. This is a static dependency/config review, not a runtime packet capture.
- WebView: Safe Browsing enabled (API 26+), mixed content disallowed, SSL failures cancelled, and third-party cookies disabled. Cleartext HTTP remains globally enabled for browser/downloader compatibility.
- **Outstanding WebView security review:** `QtSniffer` and `QtFetch` JavaScript interfaces are attached to the browsing WebView and exposed to page JavaScript across arbitrary sites. `QtFetch` only returns results for pending IDs, but no per-origin bridge allowlist is visible. Before release, review whether media-sniffing bridges can be restricted by origin/navigation lifecycle or safely redesigned. Do not treat this as resolved.
- APK signature verification with `apksigner` was not available in this runtime. APK and artifact ZIP integrity checks passed; no device install/runtime test has been performed.


## Latest iteration

- d57cc6cf CI run 46140412771 passed (debug APK build and unit tests). APK workflow 46140412687 passed and uploaded verified debug artifact ID 15261108167. ZIP integrity checks passed; artifact SHA-256 `9c36d0fa204b2812cb1cb0716ca3fca563e5921eb6d99f2ba7b0acfb56346565`; universal APK SHA-256 `e5f264cdf93818738b9239ff469351235218760bbd2786153b0f8e90b00bd74e`.
- Voice language chooser commits 7864e832 and e65616fe. The first CI run for 7864e832 was cancelled when the UI refinement landed; the latest CI and APK workflow on e65616fe were running at the time of this update. Do not claim their pass until checked.
- Current QA downloads are debug builds, not release-signed production APKs. `apksigner` is unavailable here, so signature verification and device installation/runtime tests remain unperformed.
