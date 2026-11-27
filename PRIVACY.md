# Privacy — DUO privacy-focused fork

DUO has no accounts and no server of its own. Downloads, history, vault, playlists and settings are stored on-device.

## Data collection and telemetry

This fork removes Firebase Analytics and Crashlytics dependencies and uses a no-op analytics implementation. It does not send analytics events, crash reports, screen-view events, search terms, titles, filenames, or country/device telemetry to an analytics provider.

## Automatic network activity

Automatic app-update checks, yt-dlp engine-update checks, and background channel polling are disabled in this fork. App updates may still be checked manually from Settings, which contacts GitHub and exposes the device's IP address to GitHub.

## Network needed for app features

When you actively browse, search, stream or download media, DUO must make requests to the websites and media hosts you choose. Those services receive ordinary network request information such as your IP address and the URL requested. This traffic is required for those online features and is not telemetry. Offline files can be played without these media requests.

The app is not a network firewall: it cannot block all network traffic while still providing online search, browsing, streaming, and downloads. For fully offline use, do not use online features.
