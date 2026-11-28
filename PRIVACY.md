# Privacy

DUO Tube does not include analytics or crash-reporting SDKs. The analytics abstraction is no-op. The unused games Remote Config fetch was removed.

The app makes network requests when the user searches, opens websites, or downloads media. Media extraction may contact the website/provider the user selected. Browser and downloader HTTP support remains enabled for user-directed HTTP links; HTTPS pages block mixed HTTP subresources and WebView SSL errors are cancelled.

Voice search uses the Android system speech-recognition service. Depending on the provider and device, audio may be processed off-device. DUO does not record or store voice audio. Microphone permission is requested only after the user invokes voice search.

Media/gallery access is requested only for the related feature. APK installation/updating is not an in-app feature and no installer permission is requested.

The exported MediaSession is used for external playback controls. Media3 default policy gives untrusted controllers read-only access; trusted controllers may control playback.

WebView JavaScript bridges are used for media sniffing/extraction. They remain a security-review item because they are available to page scripts. URL filtering and WebView Safe Browsing are enabled, but URL filters cannot classify all images/videos hosted on allowed sites.
