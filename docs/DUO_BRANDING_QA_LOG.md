# DUO-Tube Branding & QA Log

## Iteration 2 — UI, privacy, and security

- DUO branding and vector splash/welcome artwork retained.
- Quick website suggestions are limited to YouTube, Instagram, and Facebook.
- Status Saver is reachable through bottom navigation and opens directly.
- About/search branding updated; Quick Apps/Games promotional flow removed.
- In-app updater/APK installation flow and install permission removed.
- Analytics/crash telemetry SDKs and unused games Remote Config fetch removed.
- Voice-search permission is requested only when voice search is invoked; speech results fill the query without auto-submit.
- WebView Safe Browsing enabled; mixed content is blocked on HTTPS pages; SSL errors are cancelled.
- URL/content filtering is applied across browser, search, share, download-sheet, and bulk routes. It cannot classify all images/videos hosted on allowed sites.

### Validation

- Commit 6cefe7c9: CI and APK build passed.
- Later iterations exposed test/build failures. Fixes were committed, but latest tests are pending or logs unavailable.
- Latest main SHA: 3e2c7688d7b4692589bd3dc9b13e164aed300c2c. No latest QA APK has been verified.

### Remaining

- Review cleartext HTTP support, JavaScript bridges, and exported MediaSession access.
- Device QA: voice recognition, permission denial/grant, Status Saver, downloads, browser, PiP, and UI.
