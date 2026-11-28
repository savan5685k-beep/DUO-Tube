# DUO Tube production-readiness progress

Updated: 2026-11-28

## Latest iteration (games/Remote Config removal and compile fix)

- Removed Android Firebase Remote Config games fetch, desktop Firebase REST fetch, cached games data/model, games screens and openers, and their test/template docs.
- Removed Firebase Remote Config dependencies from `core:data`, the unused Google Services/Crashlytics plugin aliases, and desktop build-time generation of Firebase client config.
- Updated README and privacy docs: no in-app updater/APK installer, no analytics/crash telemetry, quick suggestions are only YouTube/Instagram/Facebook; voice-recognition provider disclosure added.
- The first removal commit accidentally copied the common `expect` analytics declarations into the Android platform file instead of its `actual` Koin binding. CI/APK builds failed; this fix restores the Android no-op `actual` binding and removes the stale “games” platform comment.
- Previous verified main `d1e044809970ce9f763544403bf71f0e9b6c199d` has successful CI and APK build (run 46131065044 and 46131065119). The QA artifact SHA-256 is `a04fd84fdee03e270cacd73bcc4cf84ae53aa2ef9b118570f05cfd7db0e74b2e`; it contains arm64, armeabi-v7a, and universal debug APKs. New fix commit validation is pending.

## Earlier iteration

### Changes committed

- `a7056243` — isolated adult URL filtering from `BrowserScreen.kt`.
- `5caa6f90` — expanded conservative adult-domain list and explicit URL/search-term filtering in `BrowserContentFilter.kt`.
- `4bc4e305` — Android-compatible URL decoding for encoded filter terms.
- `47c510b2` — added `BrowserContentFilterTest.kt` covering known hosts/subdomains, deceptive suffixes, normalization, encoded terms, and ordinary URLs.
- `e8171733` — guard platform WebView Safe Browsing setting on API 26+ (minSdk 24).
- `673e0b84`, `0aa3b664`, `070822fb`, `33661d90` — remove the in-app APK updater from app startup, Settings, DI, dependencies, and the project; delete `core/update` module sources. DUO no longer has an APK-install/update feature or install-from-unknown-sources flow in the app.
- `ccba880d`, `aabec2df`, `9cbe2d10`, `13ec78a6`, `7138e174`, `d44702b0`, `8140d55e` — add Android voice-search UI with Hindi/English selection, runtime microphone permission requested only after choosing voice search, Android SpeechRecognizer integration, desktop text-only fallback, and required Activity Compose dependency.

### Files touched

- `feature/browser/.../BrowserScreen.kt`
- `feature/browser/.../BrowserContentFilter.kt` (new)
- `feature/browser/src/test/.../BrowserContentFilterTest.kt` (new)
- `feature/search/.../SearchScreen.kt`
- `feature/search/.../VoiceSearchControl.kt` (new expect declaration)
- `feature/search/src/androidMain/.../VoiceSearchControl.android.kt` (new)
- `feature/search/src/desktopMain/.../VoiceSearchControl.desktop.kt` (new)
- `feature/search/src/androidMain/AndroidManifest.xml` (new microphone permission)
- `feature/search/build.gradle.kts`
- `app/build.gradle.kts`
- `app/src/main/.../MainActivity.kt`
- `app/src/main/.../QuickTubeApplication.kt`
- `app/src/main/.../di/AppModule.kt`
- `feature/settings/build.gradle.kts`
- `feature/settings/src/main/.../SettingsScreen.kt`
- `settings.gradle.kts`
- Removed `core/update` module files and tests.

## Tests and CI

- Commit `ccb91c93` (games/Remote Config removal): CI [46132709052](https://github.com/savan5685k-beep/DUO-Tube/actions/runs/46132709052) — **failed**; APK build [46132708939](https://github.com/savan5685k-beep/DUO-Tube/actions/runs/46132708939) — **failed**. Root cause: Android analytics actual binding was missing after the no-op file rename.
- GitLab mirror for `ccb91c93`: [46132709013](https://github.com/savan5685k-beep/DUO-Tube/actions/runs/46132709013) — success.
- Fix commit restores the Android Koin `actual analyticsModule` binding; its CI/APK runs are pending.
- Previous verified main `d1e04480`: CI [46131065044](https://github.com/savan5685k-beep/DUO-Tube/actions/runs/46131065044) and APK build [46131065119](https://github.com/savan5685k-beep/DUO-Tube/actions/runs/46131065119) — success; artifact SHA-256 `a04fd84fdee03e270cacd73bcc4cf84ae53aa2ef9b118570f05cfd7db0e74b2e`.

## Remaining audit / blockers

- Device-level QA is still required for permission denial/grant, Hindi/English speech recognition availability, WebView behavior, downloads, WhatsApp Saver, and update distribution via manual GitHub Releases.
- Android voice recognition uses the device/system speech service; that provider may process audio off-device. DUO itself does not record or upload voice audio.
- URL/domain filtering cannot guarantee classification of explicit image/video content hosted on allowed sites.
- `usesCleartextTraffic=true` remains under review because the browser/downloader may intentionally support HTTP.
- Verify that Remote Config/game-fetch code and Firebase dependencies are absent from the current tree and CI output.
- Exported MediaSession service is intentional for external media controls; controller access policy remains under review.
- Production readiness is not declared.
