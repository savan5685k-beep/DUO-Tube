# DUO release signing

The repository is a fork of QuickTube. Source-code ancestry does not transfer the upstream project's Android signing key.

## First DUO release

No GitHub Release is currently published in this repository. Before creating a DUO release key, check whether a DUO APK has already been shared privately or installed on devices. If one exists, its signing certificate must be inspected and preserved for updates.

The release workflow currently pins this expected certificate SHA-256:

`b5fb3a7defdc7a3e0c0d6c3b468bc6353cd22225bbfee0585d7f337667aeb87a`

This is a public certificate fingerprint, not a password. It is an existing workflow expectation and must not be replaced with a guessed value. After the actual DUO keystore is prepared, compare its certificate fingerprint locally. Only then update `EXPECTED_CERT_SHA256` in `.github/workflows/release.yml` to the actual verified fingerprint.

## Required GitHub Actions secrets

Set these in repository Settings → Secrets and variables → Actions:

- `RELEASE_KEYSTORE_BASE64`
- `RELEASE_KEYSTORE_PASSWORD`
- `RELEASE_KEY_ALIAS`
- `RELEASE_KEY_PASSWORD`

Optional:

- `GOOGLE_SERVICES_JSON`

Never commit the keystore, passwords, base64 keystore contents, or Firebase private configuration. Never send them in chat or issue comments. Keep at least two secure offline backups of the keystore and store passwords in a password manager.

## Local checks

Use `keytool -list -v -keystore <keystore-file> -alias <alias>` to inspect the certificate. Record the SHA-256 fingerprint and compare it locally with the release workflow. Do not publish the keystore itself.

Do not generate a new key if a DUO-signed APK has already been distributed unless you have established that the new certificate is compatible with the installed app update path.
