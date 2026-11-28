# Privacy — DUO privacy-focused fork

DUO has no accounts and no server of its own. Downloads, history, vault, playlists and settings are stored on-device.

## Data collection and telemetry

This fork removes Firebase Analytics and Crashlytics dependencies and uses a no-op analytics implementation. It does not send analytics events, crash reports, screen-view events, search terms, titles, filenames, or country/device telemetry to an analytics provider.

## Automatic network activity

DUO has no in-app APK updater or installer. App updates are distributed through the official release page and installed only through Android's user-controlled install flow. No in-app update check contacts GitHub. Automatic channel polling and automatic yt-dlp engine-update checks are disabled in this fork. Firebase Remote Config is not used.

## Network needed for app features

When you actively browse, search, stream or download media, DUO must make requests to the websites and media hosts you choose. Those services receive ordinary network request information such as your IP address and the URL requested. This traffic is required for those online features and is not telemetry. Offline files can be played without these media requests.

Voice search uses the Android system speech-recognition service; the chosen provider may process audio off-device. DUO itself does not record or upload voice audio.

The app is not a network firewall: it cannot block all network traffic while still providing online search, browsing, streaming, and downloads. For fully offline use, do not use online features.
