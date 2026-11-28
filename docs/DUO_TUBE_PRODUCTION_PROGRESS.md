# DUO Tube production-readiness progress

Updated: 2026-11-28

## Current main

- SHA at this status update: cbfa0a23765e99b555e45672dcf1924d46a05f16 (documentation commit after the signing-pin changes).
- Includes the branding, voice-search, privacy, security, filtering, and download-flow changes plus the QA-install workflow correction.


## Implemented

- DUO branding, vector splash artwork, and welcome UI.
- Removed Quick Apps/Games promotional flow and unused games-fetch dependency.
- Status Saver is available through bottom navigation and opens its saver screen directly.
- About/search branding updated; quick website choices restricted to YouTube, Instagram, and Facebook.
- In-app updater/APK installation flow and install permission removed.
- Analytics/crash telemetry SDKs and unused games Remote Config fetch removed; analytics abstraction is no-op.
- Voice-search permission is requested only when the user invokes voice search. Voice results fill the query without auto-submit.
- Android voice search now offers Auto, Hindi (`hi-IN`), and English (`en-IN`) modes. CI passed on commit aed2b23f4e94ad9d4c42cd315290916ec1d73dd7; device speech-provider availability remains unverified.
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

## Installation-conflict QA iteration (2026-11-28)

- Screenshot reported Android package-conflict installation error. The likely cause was the CI debug APK using the production package ID and a runner-generated debug signing certificate, which can differ between GitHub runners. A release-signed install also cannot be updated with a debug-signed APK.
- Commit 7534fc1d switches the automated APK workflow to the optimized QA variant with package ID suffix `.qa`, so it installs alongside the production app. Both main and manual QA workflows cache a dedicated QA debug keystore and serialize builds to avoid competing first-run keys.
- CI run 46146211710 on aed2b23f: **passed** (debug build and unit tests).
- QA APK workflow run 46146198322 on 7534fc1d: **passed**. It built the arm64, arm32, and universal QA APKs, verified signatures and `com.savan5685k.duo.qa` package IDs with Android build tools, ran tests, and uploaded artifact ID 15263584207.
- Verified QA certificate SHA-256: `491877412845f1874c2003b9ebc0db3895859d0c95f1421a36be3344efc99862`. Workflow commits 458a30ca and 26ac32b4 pin this public debug-certificate fingerprint so cache loss/key rotation fails closed instead of silently producing an incompatible update. These fingerprint-pin workflow commits are awaiting their own CI/build validation.
- Artifact ZIP SHA-256: `6833c32b3ad93a2d33dc7676eb2d7cc76a59fb2467219a92f4453c582d5df362`; ZIP integrity passed. Universal APK SHA-256: `79003b121305c77a39f4c57b78afa39037d8c9daff21badee819ceb896cedf4f`; arm64 APK SHA-256: `2187a6dc5bb796c51be7e7e19b59d0c5ac4fa06b86ddbed36831d963f056162a`.
- Artifact expires 2026-12-12. This is a debug-signed QA variant, not a production release. Its `.qa` package can coexist with the installed DUO package; it does not replace/update a release-signed production installation.

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
- APK signatures and package IDs are now verified in GitHub Actions with `apksigner` and `apkanalyzer`; artifact ZIP integrity is checked after download. No on-device install/update/runtime test has been performed.


## Latest iteration

- Latest workflow-code commits 7534fc1d, aed2b23f, 458a30ca, and 26ac32b4 implement QA package separation, stable debug signing cache, and a pinned QA certificate. CI on aed2b23f passed; QA build/test/signature/package verification on 7534fc1d passed. The fingerprint-pin commits are awaiting their latest main CI/APK workflow results.
- Last verified QA APK artifact is from 7534fc1d, artifact ID 15263584207. It contains three APKs; all signatures and `.qa` package IDs were verified and ZIP integrity passed. A build on the fingerprint-pinned latest main is still required before calling it the latest artifact.
- Production readiness is not declared. Device QA and the WebView JavaScript-bridge security review remain outstanding.


## User feedback iteration — 2026-11-28

- User-rated current QA app 9/10 and reported a launch flash: Android system splash showed a dark background and app icon before the animated welcome. Android's system splash is platform-mandatory, so removed its drawable icon (transparent vector) to eliminate the unwanted logo flash; the welcome screen remains the app's own animation. This is a UI handoff improvement, not removal of the OS splash itself.
- User's new requested backlog: persistent dismissed clipboard suggestion via right-swipe animation; advanced swipe-to-PiP and resizable PiP; background audio mode with thumbnail/headphones artwork and in-player return-to-video control; one tap Auto/double tap English/triple tap Hindi voice modes with append-to-existing-query behavior; crash investigation; repository-backed update checker and release workflow; in-app status photo/video viewer; status-owner display; move Status Saver to tab position 4 (next to Downloads). These are queued for scoped implementation and QA; no claim of completion yet.


## User-requested 9/10 feedback iteration — 2026-11-28 (implementation in progress)

- `743f0287`: system launch splash icon made transparent to remove the unwanted black/icon flash before welcome. Android's platform splash itself cannot be removed; its icon is now blank.
- `ecdfbf6f` + `d9a60c07`: copied-link card's close button removed; rightward horizontal swipe dismisses the card and its dismissed URL is remembered with saveable state. The card uses the existing fade/shrink exit animation. CI validation pending.
- `c8a4e4fb`: successive voice-search results now append to existing query text instead of replacing it.
- `3e1e833e`: mic tap-count selection replaces the language popup: 1 tap Auto, 2 taps English (en-IN), 3+ taps Hindi (hi-IN), with a short tap aggregation window. Permission remains on-demand. CI validation pending.
- `99627860`: About screen includes a Check for updates entry opening the official repository Releases page. No release is published yet; this is a manual official-source link, not an automatic APK installer/update mechanism.
- `93ad72ce`: audio-only playback now shows current artwork behind a headphones icon. Existing player controls include the Watch/Listen toggle and floating PiP recall ("Show here").
- Navigation inspection confirms Status Saver is already tab position 4, adjacent to Gallery and before Settings; no reorder was necessary.
- Status Saver currently stores only file name/URI/media type/time/size. WhatsApp contact identity is not present in this folder metadata; displaying a status owner's contact name requires a supported, privacy-preserving source and is not yet implemented.
- Status media still launches Android external viewers; an in-app status viewer remains pending. Advanced swipe-to-PiP remains pending; Android system PiP resize behavior and swipe entry need device-level testing.
- Crash is not reproducible from the available information; no telemetry is enabled. Crash root cause remains unverified.
- Latest workflow runs are pending/queued and earlier runs were cancelled by subsequent commits. No validation claim is made for these new commits until the latest main CI and APK workflows complete.
