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


## Iteration 3 — Install conflict and signing stability (2026-11-28)

- Reproduced the reported installer message conceptually from the package/signing workflow: debug APKs were built under the production package ID and can be signed by runner-local debug certificates that change between CI runs. A debug APK cannot update a release-signed installation.
- Main and manual QA workflows now build the optimized `qa` variant with package suffix `.qa`, restore a dedicated cached debug key, serialize builds, and verify APK signature plus package ID.
- Verified certificate SHA-256: `491877412845f1874c2003b9ebc0db3895859d0c95f1421a36be3344efc99862`. Workflow commits pin this fingerprint to fail if signing-key cache is lost or rotated.
- QA run 46146198322 on commit 7534fc1d passed build, signature/package verification, unit tests, and artifact upload. Artifact ID 15263584207 (ZIP SHA-256 `6833c32b3ad93a2d33dc7676eb2d7cc76a59fb2467219a92f4453c582d5df362`) contains arm64, arm32, and universal QA APKs. ZIP integrity passed after download.
- The fingerprint-pinning commits and the progress-document update are awaiting validation on the newest main SHA. Device installation/update testing is still required.
