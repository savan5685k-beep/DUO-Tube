# DUO Tube production-readiness progress

Updated: 2026-11-28

## Latest iteration

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

- Latest CI run: [46129606872](https://github.com/savan5685k-beep/DUO-Tube/actions/runs/46129606872) — in progress at this update.
- Latest debug APK run: [46129606856](https://github.com/savan5685k-beep/DUO-Tube/actions/runs/46129606856) — in progress at this update.
- GitLab mirror for commit `8140d55e`: [46129606871](https://github.com/savan5685k-beep/DUO-Tube/actions/runs/46129606871) — success.
- Previous verified baseline `9dd1db0`: CI [46127956889](https://github.com/savan5685k-beep/DUO-Tube/actions/runs/46127956889), APK build [46127956909](https://github.com/savan5685k-beep/DUO-Tube/actions/runs/46127956909), mirror [46127956866](https://github.com/savan5685k-beep/DUO-Tube/actions/runs/46127956866) — all success.
- No tests for the new filter or voice-search changes are claimed passed until the current CI run completes.

## Remaining audit / blockers

- Device-level QA is still required for permission denial/grant, Hindi/English speech recognition availability, WebView behavior, downloads, WhatsApp Saver, and update distribution via manual GitHub Releases.
- Android voice recognition uses the device/system speech service; that provider may process audio off-device. DUO itself does not record or upload voice audio.
- URL/domain filtering cannot guarantee classification of explicit image/video content hosted on allowed sites.
- `usesCleartextTraffic=true` remains under review because the browser/downloader may intentionally support HTTP.
- Firebase Remote Config games repository/dependency remains in source although the games route/UI was removed; audit and remove its unused outbound fetch path in a later iteration.
- Exported MediaSession service is intentional for external media controls; controller access policy remains under review.
- Production readiness is not declared.
