# DUO Production Readiness

This checklist records the release blockers identified during a repository audit. It is not a claim that the app is production-ready.

## Release blockers

- [ ] **Unique Android application identity:** `app/build.gradle.kts` still uses `com.edureminder.quicktube`. Choose and migrate to a DUO-owned application ID before the first public DUO release. Audit manifest authorities, task affinities, deep links, updater identity, and package references as part of the migration.
- [ ] **Release signing secrets:** GitHub Actions needs `RELEASE_KEYSTORE_BASE64`, `RELEASE_KEYSTORE_PASSWORD`, `RELEASE_KEY_ALIAS`, and `RELEASE_KEY_PASSWORD`. Do not publish until the key is securely backed up and the actual certificate fingerprint is verified.
- [ ] **Pinned certificate:** `release.yml` currently pins SHA-256 `b5fb3a7defdc7a3e0c0d6c3b468bc6353cd22225bbfee0585d7f337667aeb87a`. Confirm this matches the real DUO release key before production use. Never replace it with an unverified value.
- [ ] **Network policy:** The manifest currently sets `android:usesCleartextTraffic="true"`. Review HTTP support and decide on a production network-security policy. The built-in browser's HTTP support means this cannot be changed blindly without testing.
- [ ] **File sharing scope:** `app/src/main/res/xml/file_paths.xml` currently grants FileProvider path coverage to the entire external-storage root (`.`), internal files, and cache. Audit actual shared-file paths and narrow the provider configuration where compatible.
- [ ] **License/attribution review:** Verify upstream notices, GPLv3 obligations, third-party notices, modified-source availability, and any bundled component licenses before distribution.

## Release verification checklist

- [ ] Confirm CI passes on the exact release commit.
- [ ] Run QA APK on at least one real Android device and record device/Android version.
- [ ] Test install, update-over-existing-install, uninstall/reinstall, and app data behavior.
- [ ] Test download pause/resume, failed download retry, storage-full handling, and background behavior.
- [ ] Test browser navigation, Safe Browsing, blocked-content checks, and HTTP/HTTPS compatibility.
- [ ] Test updater against a draft/staging release and verify ABI selection.
- [ ] Verify signed APK certificate, v1/v2 signature schemes, checksums, and APK contents.
- [ ] Confirm release notes, source tag, and reproducible version naming.
- [ ] Publish only after all blockers are checked off.

## Audit note

This checklist was prepared from a repository configuration review. It does not replace runtime testing, legal review, or independent security testing.
