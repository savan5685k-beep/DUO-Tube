# DUO-Tube Branding & QA Log

## Iteration 1 — Branding, suggestions, privacy, and security cleanup

- Renamed the fourth home tab to WhatsApp Saver and wired it to the existing saver screen.
- Removed the promotional games row, game refresh dependency, and unused games navigation.
- Restricted quick-site suggestions to YouTube, Instagram, and Facebook; removed the generic Sites suggestion button.
- Search suggestions now show only the three allowed sites; legacy built-in sites are hidden from suggestions while user-added sites remain.
- Updated About, share instructions, and backup filename prefix to DUO branding.
- Removed the app-uninstaller entry point and broad package-discovery permissions.
- Added a vector DUO splash logo and welcome screen.
- Strengthened the adult URL guard with additional explicit terms and JVM-testable domain-suffix/keyword cases.
- Analytics and crash-reporting SDKs remain absent; website requests and manual updater requests are external-service traffic, not telemetry.

### Validation and limitations

- The previous debug APK build for commit e6603368 completed successfully; later CI builds are still in progress/cancelled as newer commits superseded them.
- This adult-content filter is domain/URL-keyword based and WebView Safe Browsing; it cannot guarantee detection of explicit imagery/video embedded on allowed sites.
- Voice search, permission-on-demand refinements, and full outbound-network audit remain outstanding.
