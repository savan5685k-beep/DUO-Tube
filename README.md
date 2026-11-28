<div align="center">

# DUO-Tube

**An Android media utility project with a custom DUO-Tube identity.**

[![CI](https://github.com/savan5685k-beep/DUO-Tube/actions/workflows/ci.yml/badge.svg)](https://github.com/savan5685k-beep/DUO-Tube/actions/workflows/ci.yml)
[![License: GPL v3](https://img.shields.io/badge/license-GPL--3.0-blue)](LICENSE)
[![Android](https://img.shields.io/badge/platform-Android-green?logo=android)](https://www.android.com/)

[Latest releases](https://github.com/savan5685k-beep/DUO-Tube/releases) · [Source code](https://github.com/savan5685k-beep/DUO-Tube)

</div>

## About

DUO-Tube is an Android project being customized, tested, and maintained under its own project identity. The current test APK has been installed and checked by the maintainer. New features and additional polish are planned for later updates; the current focus is on the tested build, repository cleanup, and a dependable release process.

This repository is based on the open-source QuickTube project. Original upstream attribution and applicable license notices are retained. DUO-Tube-specific changes and branding are maintained in this repository.

## Current status

- **Test APK:** Maintainer-tested; accepted for the current test milestone.
- **Public release:** Not published yet.
- **Next milestone:** Repository branding cleanup and first official release.
- **Future updates:** Features and improvements will be added incrementally after this test milestone.

## Download

Official builds will be published on the [GitHub Releases page](https://github.com/savan5685k-beep/DUO-Tube/releases). Until a release is published, there is no official public release APK listed here.

## Project information

- Android application project
- Kotlin-based codebase
- GPL-3.0 license (see [LICENSE](LICENSE))
- Build and QA automation through GitHub Actions

## Build from source

Prerequisites: Android Studio or a compatible Java/Android SDK environment.

```bash
./gradlew :app:assembleDebug
```

Run unit tests:

```bash
./gradlew testDebugUnitTest
```

## Privacy

Read the project privacy notes in [PRIVACY.md](PRIVACY.md). The app makes network requests for user-initiated searches, website browsing, and media operations. Review the privacy document and app permissions before use.

## Responsible use

Use the app only for content you own, are authorized to save, or that the relevant service permits you to download. Respect applicable laws, platform terms, copyright, and creators' rights. DUO-Tube is not affiliated with YouTube, Google, or other third-party services.

## Credits and licenses

DUO-Tube is derived from the upstream [QuickTube project](https://github.com/devgalib/QuickTube). This repository retains upstream license and attribution obligations. See [LICENSE](LICENSE) and the source notices.

Third-party libraries and components remain subject to their respective licenses.

## Roadmap

- [x] Test milestone APK reviewed by maintainer
- [ ] Complete repository and visual-brand cleanup
- [ ] Replace repository imagery with maintainer-captured DUO-Tube screenshots
- [ ] Publish first official GitHub Release
- [ ] Add further features and refinements in later updates

## Contributing

Contributions and bug reports can be submitted through this repository when its issue and contribution channels are enabled. Please review the project license and upstream notices before proposing changes.
